package br.alura.ForumHub.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.alura.ForumHub.domain.valueobject.Username;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private UUID id;
  private String name;
  private Username username;
  private String email;
  private String password;
  private LocalDateTime createdAt;
  private boolean isActive;

  public User(String name, String username, String email, String password) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.username = new Username(username);
    this.email = email;
    this.password = password;
    this.createdAt = LocalDateTime.now();
    this.isActive = true;
  }

  public void deactivate() {
    this.isActive = false;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isActive() {
    return isActive;
  }
}
