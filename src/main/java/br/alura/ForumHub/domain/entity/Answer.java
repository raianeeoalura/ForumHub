package br.alura.ForumHub.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Answer {

  private UUID id;
  private String content;
  private UUID authorId;
  private UUID topicId;
  private Integer viewCount;
  private LocalDateTime createdAt;

  public Answer(String content, UUID authorId, UUID topicId) {
    this.id = UUID.randomUUID();
    this.content = content;
    this.authorId = authorId;
    this.topicId = topicId;
    this.viewCount = 0;
    this.createdAt = LocalDateTime.now();
  }
}
