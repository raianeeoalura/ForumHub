package br.alura.ForumHub.infra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.alura.ForumHub.application.usecase.topic.CreateTopicUseCase;
import br.alura.ForumHub.application.usecase.topic.CreateTopicUseCase.CreateTopicRequest;
import br.alura.ForumHub.infra.dto.BasicTopicData;
import br.alura.ForumHub.infra.dto.CreateTopicRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
  private final CreateTopicUseCase createTopicUseCase;

  @PostMapping
  public ResponseEntity<BasicTopicData> createTopic(@RequestBody @Valid CreateTopicRequestDTO body,
      UriComponentsBuilder uriBuilder) {
    var data = new CreateTopicRequest(
        body.title(),
        body.content(),
        body.authorId());

    var topic = createTopicUseCase.execute(data);
    var uri = uriBuilder.path("/topics/{id}")
        .buildAndExpand(topic.getId())
        .toUri();

    var response = new BasicTopicData(topic);
    return ResponseEntity.created(uri).body(response);
  }
}
