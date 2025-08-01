package br.alura.ForumHub.infra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.alura.ForumHub.application.usecase.answer.CreateAnswerUseCase;
import br.alura.ForumHub.application.usecase.answer.CreateAnswerUseCase.CreateAnswerRequest;
import br.alura.ForumHub.infra.dto.BasicAnswerData;
import br.alura.ForumHub.infra.dto.CreateAnswerRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {
  private final CreateAnswerUseCase createAnswerUseCase;

  @PostMapping
  public ResponseEntity<BasicAnswerData> createAnswer(@RequestBody @Valid CreateAnswerRequestDTO body,
      UriComponentsBuilder uriBuilder) {
    var data = new CreateAnswerRequest(
        body.content(),
        body.topicId(),
        body.authorId());

    var answer = createAnswerUseCase.execute(data);
    var uri = uriBuilder.path("/answers/{id}")
        .buildAndExpand(answer.getId())
        .toUri();

    var response = new BasicAnswerData(answer);
    return ResponseEntity.created(uri).body(response);
  }
}
