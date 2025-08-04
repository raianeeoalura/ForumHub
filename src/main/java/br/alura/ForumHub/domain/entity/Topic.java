package br.alura.ForumHub.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.alura.ForumHub.domain.valueobject.Slug;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Topic {

  private UUID id;
  private String title;
  private String content;
  private UUID authorId;
  private Slug slug;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private boolean isActive;
  private int viewCount;

  public Topic(String title, String content, UUID authorId) {
    this.id = UUID.randomUUID();
    this.title = title;
    this.content = content;
    this.slug = Slug.createFromTextWithSuffix(title, this.id.toString());
    this.authorId = authorId;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = null;
    this.isActive = true;
    this.viewCount = 0;
  }

  public void incrementViewCount() {
    this.viewCount++;
  }

  public boolean getIsActive() {
    return isActive;
  }
}
