package br.alura.ForumHub.infra.dto;

public record UpdateTopicRequestDTO(
    String title,
    String content) {
}
