package br.alura.ForumHub.infra.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.alura.ForumHub.domain.entity.Answer;

public record BasicAnswerData(UUID id, String content, UUID topicId, UUID authorId,
    LocalDateTime createdAt,
    Integer viewCount) {

  public BasicAnswerData(Answer answer) {
    this(
        answer.getId(),
        answer.getContent(),
        answer.getTopicId(),
        answer.getAuthorId(),
        answer.getCreatedAt(),
        answer.getViewCount());
  }
}
