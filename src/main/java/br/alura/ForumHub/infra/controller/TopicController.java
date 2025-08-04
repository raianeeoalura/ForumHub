package br.alura.ForumHub.infra.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.alura.ForumHub.application.usecase.topic.CreateTopicUseCase;
import br.alura.ForumHub.application.usecase.topic.CreateTopicUseCase.CreateTopicRequest;
import br.alura.ForumHub.application.usecase.topic.DeleteTopicByIdUseCase;
import br.alura.ForumHub.application.usecase.topic.DeleteTopicByIdUseCase.DeleteTopicRequest;
import br.alura.ForumHub.application.usecase.topic.GetTopicWithAnswersUseCase;
import br.alura.ForumHub.application.usecase.topic.GetTopicWithAnswersUseCase.GetTopicWithAnswersRequest;
import br.alura.ForumHub.application.usecase.topic.GetTopicsUseCase;
import br.alura.ForumHub.application.usecase.topic.GetTopicsUseCase.GetTopicsUseCaseRequest;
import br.alura.ForumHub.application.usecase.topic.UpdateTopicByIdUseCase;
import br.alura.ForumHub.application.usecase.topic.UpdateTopicByIdUseCase.UpdateTopicRequest;
import br.alura.ForumHub.infra.dto.BasicTopicData;
import br.alura.ForumHub.infra.dto.CreateTopicRequestDTO;
import br.alura.ForumHub.infra.dto.TopicWithAnswerData;
import br.alura.ForumHub.infra.dto.UpdateTopicRequestDTO;
import br.alura.ForumHub.infra.persistence.entity.UserDB;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
  private final CreateTopicUseCase createTopicUseCase;
  private final GetTopicsUseCase getTopicsUseCase;
  private final GetTopicWithAnswersUseCase getTopicWithAnswersUseCase;
  private final UpdateTopicByIdUseCase updateTopicUseCase;
  private final DeleteTopicByIdUseCase deleteTopicUseCase;

  @PostMapping
  @SecurityRequirement(name = "bearer-key")
  public ResponseEntity<BasicTopicData> createTopic(@RequestBody @Valid CreateTopicRequestDTO body,
      UriComponentsBuilder uriBuilder, @AuthenticationPrincipal UserDB user) {
    var data = new CreateTopicRequest(
        body.title(),
        body.content(),
        user.getId().toString());

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
  public ResponseEntity<TopicWithAnswerData> getTopicWithAnswers(@PathVariable String id,
      @PageableDefault(size = 10) Pageable pageable) {
    var data = new GetTopicWithAnswersRequest(id, pageable.getPageNumber(), pageable.getPageSize());
    var topicWithAnswers = getTopicWithAnswersUseCase.execute(data);
    var dto = new TopicWithAnswerData(topicWithAnswers.getTopic(), topicWithAnswers.getAnswers());

    return ResponseEntity.ok(dto);
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearer-key")
  public ResponseEntity<Object> updateTopicById(@PathVariable String id, @RequestBody UpdateTopicRequestDTO body,
      @AuthenticationPrincipal UserDB user) {
    var data = new UpdateTopicRequest(
        id,
        user.getId().toString(),
        body.title(),
        body.content());

    updateTopicUseCase.execute(data);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearer-key")
  public ResponseEntity<Object> deleteTopicById(@PathVariable String id, @AuthenticationPrincipal UserDB user) {
    var data = new DeleteTopicRequest(id, user.getId().toString());
    deleteTopicUseCase.execute(data);
    return ResponseEntity.noContent().build();
  }
}
