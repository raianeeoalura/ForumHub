package br.alura.ForumHub.infra.controller;

import br.alura.ForumHub.application.usecase.topic.GetTopicWithAnswersUseCase;
import br.alura.ForumHub.application.usecase.topic.GetTopicWithAnswersUseCase.GetTopicWithAnswersRequest;
import br.alura.ForumHub.application.usecase.topic.GetTopicsUseCase;
import br.alura.ForumHub.application.usecase.topic.GetTopicsUseCase.GetTopicsUseCaseRequest;
import br.alura.ForumHub.infra.dto.TopicWithAnswerData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.alura.ForumHub.application.usecase.topic.CreateTopicUseCase;
import br.alura.ForumHub.application.usecase.topic.CreateTopicUseCase.CreateTopicRequest;
import br.alura.ForumHub.infra.dto.BasicTopicData;
import br.alura.ForumHub.infra.dto.CreateTopicRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
  private final CreateTopicUseCase createTopicUseCase;
  private final GetTopicsUseCase getTopicsUseCase;
  private final GetTopicWithAnswersUseCase getTopicWithAnswersUseCase;

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

  @GetMapping
  public ResponseEntity<List<BasicTopicData>> listTopics(Pageable pageable) {
    var data = new GetTopicsUseCaseRequest(pageable.getPageNumber(), pageable.getPageSize());
    var topics = getTopicsUseCase.execute(data).stream().map(BasicTopicData::new).toList();

    return ResponseEntity.ok(topics);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TopicWithAnswerData> getTopicWithAnswers(@PathVariable String id, @PageableDefault(size = 10) Pageable pageable) {
    var data = new GetTopicWithAnswersRequest(id, pageable.getPageNumber(), pageable.getPageSize());
    var topicWithAnswers = getTopicWithAnswersUseCase.execute(data);
    var dto = new TopicWithAnswerData(topicWithAnswers.getTopic(), topicWithAnswers.getAnswers());

    return ResponseEntity.ok(dto);
  }
}
