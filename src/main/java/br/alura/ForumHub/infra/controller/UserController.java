package br.alura.ForumHub.infra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.alura.ForumHub.application.usecase.user.CreateUserUseCase;
import br.alura.ForumHub.application.usecase.user.CreateUserUseCase.CreateUserRequest;
import br.alura.ForumHub.infra.dto.BasicUserData;
import br.alura.ForumHub.infra.dto.CreateUserWithCredentialsRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final CreateUserUseCase createUserUseCase;

  @PostMapping
  public ResponseEntity<BasicUserData> createUser(@RequestBody @Valid CreateUserWithCredentialsRequest body,
      UriComponentsBuilder uriBuilder) {
    var data = new CreateUserRequest(
        body.name(),
        body.username(),
        body.email(),
        body.password());

    var user = createUserUseCase.execute(data);
    var uri = uriBuilder.path("/users/{id}")
        .buildAndExpand(user.getId())
        .toUri();

    var response = new BasicUserData(user);
    return ResponseEntity.created(uri).body(response);
  }
}
