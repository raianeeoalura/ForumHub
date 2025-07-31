package br.alura.ForumHub.infra.persistence.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.alura.ForumHub.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDB implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "is_active", nullable = false)
  private boolean isActive;

  @OneToMany(mappedBy = "author")
  private List<TopicDB> topics;

  public UserDB(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.username = user.getUsername().getValue();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.createdAt = user.getCreatedAt();
    this.isActive = user.isActive();
  }

  public UserDB(UUID userId) {
    this.id = userId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void addTopic(TopicDB topic) {
    topic.setAuthor(this);
    topics.add(topic);
  }
}
