package br.alura.ForumHub.infra.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTopicRequestDTO(
    @NotBlank(message = "Title cannot be blank") String title,
    @NotBlank(message = "Content cannot be blank") String content) {
}
