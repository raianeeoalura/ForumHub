package br.alura.ForumHub.infra.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import br.alura.ForumHub.domain.entity.Topic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicDB {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(unique = true, nullable = false)
  private String slug;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private UserDB author;

  @Column(name = "view_count", nullable = false)
  private Integer viewCount;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "is_active", nullable = false)
  private boolean isActive;

  @Column(name = "updated_at", nullable = true)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
  private List<AnswerDB> answers;

  public TopicDB(Topic topic) {
    this.id = topic.getId();
    this.title = topic.getTitle();
    this.content = topic.getContent();
    this.slug = topic.getSlug().getValue();
    this.author = new UserDB(topic.getAuthorId());
    this.viewCount = topic.getViewCount();
    this.createdAt = topic.getCreatedAt();
    this.isActive = topic.getIsActive();
    this.updatedAt = topic.getUpdatedAt();
  }

  public TopicDB(UUID topicId) {
    this.id = topicId;
  }
}
