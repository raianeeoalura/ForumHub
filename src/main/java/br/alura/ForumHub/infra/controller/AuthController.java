package br.alura.ForumHub.infra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.alura.ForumHub.infra.dto.AuthenticateWithCredentialsRequest;
import br.alura.ForumHub.infra.dto.CredentialsLoginResponse;
import br.alura.ForumHub.infra.persistence.entity.UserDB;
import br.alura.ForumHub.infra.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager manager;
  private final TokenService tokenService;

  @PostMapping
  public ResponseEntity<CredentialsLoginResponse> login(@RequestBody @Valid AuthenticateWithCredentialsRequest body) {
    var credentialsToken = new UsernamePasswordAuthenticationToken(body.login(), body.password());
    var authentication = manager.authenticate(credentialsToken);

    var token = tokenService.generateToken((UserDB) authentication.getPrincipal());
    return ResponseEntity.ok(new CredentialsLoginResponse(token));
  }
}
