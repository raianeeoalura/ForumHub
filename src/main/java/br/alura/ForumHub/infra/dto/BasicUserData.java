package br.alura.ForumHub.infra.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import br.alura.ForumHub.domain.entity.User;

public record BasicUserData(UUID id, String name, String username, String email, LocalDateTime createdAt,
    Boolean isActive) {

  public BasicUserData(User user) {
    this(
        user.getId(),
        user.getName(),
        user.getUsername().getValue(),
        user.getEmail(),
        user.getCreatedAt(),
        user.isActive());
  }
}
