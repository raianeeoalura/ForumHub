package br.alura.ForumHub.domain.repository;

import java.util.Optional;
import java.util.UUID;

import br.alura.ForumHub.domain.entity.User;

public interface UserRepository {
  User save(User user);

  Optional<User> findById(UUID id);

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);
}
