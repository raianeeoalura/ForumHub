package br.alura.ForumHub.infra.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.alura.ForumHub.domain.entity.Topic;

public record BasicTopicData(UUID id, String title, String content, UUID authorId, String slug, LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Boolean isActive, Integer viewCount) {

  public BasicTopicData(Topic topic) {
    this(
        topic.getId(),
        topic.getTitle(),
        topic.getContent(),
        topic.getAuthorId(),
        topic.getSlug().getValue(),
        topic.getCreatedAt(),
        topic.getUpdatedAt(),
        topic.getIsActive(),
        topic.getViewCount());
  }
}
