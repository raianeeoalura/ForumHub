package br.alura.ForumHub.infra.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import br.alura.ForumHub.domain.entity.Answer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDB {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private UserDB author;

  @ManyToOne
  @JoinColumn(name = "topic_id", nullable = false)
  private TopicDB topic;

  @Column(name = "view_count", nullable = false)
  private Integer viewCount;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  public AnswerDB(Answer answer) {
    this.id = answer.getId();
    this.content = answer.getContent();
    this.viewCount = answer.getViewCount();
    this.createdAt = answer.getCreatedAt();
  }
}
