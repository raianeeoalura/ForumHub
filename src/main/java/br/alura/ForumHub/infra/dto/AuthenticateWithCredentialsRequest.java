package br.alura.ForumHub.infra.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateWithCredentialsRequest(
    @NotBlank String login,
    @NotBlank String password) {
}
