package br.alura.ForumHub.infra.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAnswerRequestDTO(
    @NotBlank(message = "Content cannot be blank") String content,
    @NotBlank(message = "Topic ID cannot be blank") String topicId) {
}
