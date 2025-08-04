package br.alura.ForumHub.infra.dto;

import br.alura.ForumHub.domain.entity.Answer;
import br.alura.ForumHub.domain.entity.Topic;
import br.alura.ForumHub.domain.valueobject.TopicWithAnswers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record TopicWithAnswerData(UUID id, String title, String content, UUID authorId, String slug, LocalDateTime createdAt,
                                  LocalDateTime updatedAt,
                                  Boolean isActive, Integer viewCount, List<BasicAnswerData> answers) {

  public TopicWithAnswerData(Topic topic, List<Answer> answers) {
    this(
            topic.getId(),
            topic.getTitle(),
            topic.getContent(),
            topic.getAuthorId(),
            topic.getSlug().getValue(),
            topic.getCreatedAt(),
            topic.getUpdatedAt(),
            topic.getIsActive(),
            topic.getViewCount(),
            answers.stream().map(BasicAnswerData::new).collect(Collectors.toList()));
  }
}
