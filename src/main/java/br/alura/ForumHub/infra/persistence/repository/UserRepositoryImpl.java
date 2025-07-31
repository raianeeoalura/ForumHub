package br.alura.ForumHub.infra.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.alura.ForumHub.infra.persistence.entity.TopicDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.alura.ForumHub.domain.entity.Topic;
import br.alura.ForumHub.domain.entity.User;
import br.alura.ForumHub.domain.repository.UserRepository;
import br.alura.ForumHub.domain.valueobject.Username;
import br.alura.ForumHub.infra.persistence.entity.UserDB;

interface UserJpaRepository extends JpaRepository<UserDB, UUID> {
  Optional<UserDB> findByEmail(String email);

  Optional<UserDB> findByUsername(String username);
}

@Repository
public class UserRepositoryImpl implements UserRepository {
  @Autowired
  private UserJpaRepository userJpaRepository;

  @Autowired
  private TopicJpaRepository topicJpaRepository;

  public UserDB findByLogin(String username) {
    return userJpaRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
  }

  public void deleteAll() {
    userJpaRepository.deleteAll();
  }

  @Override
  public User save(User user) {
    List<TopicDB> topics = topicJpaRepository.findByAuthorId(user.getId());

    UserDB entity = new UserDB(
        null,
        user.getName(),
        user.getUsername().getValue(),
        user.getEmail(),
        user.getPassword(),
        user.getCreatedAt(),
        user.isActive(),
        topics);

    UserDB savedUser = userJpaRepository.save(entity);

    return toDomain(savedUser);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return userJpaRepository.findById(id)
        .map(this::toDomain);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userJpaRepository.findByEmail(email)
        .map(this::toDomain);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userJpaRepository.findByUsername(username)
        .map(this::toDomain);
  }

  private User toDomain(UserDB dbUser) {
    User user = new User(
        dbUser.getId(),
        dbUser.getName(),
        new Username(dbUser.getUsername()),
        dbUser.getEmail(),
        dbUser.getPassword(),
        dbUser.getCreatedAt(),
        dbUser.isActive());

    return user;
  }

  private UserDB toEntity(User user, List<Topic> topics) {
    return new UserDB(
        user.getId(),
        user.getName(),
        user.getUsername().getValue(),
        user.getEmail(),
        user.getPassword(),
        user.getCreatedAt(),
        user.isActive(),
        topics.stream().map(TopicDB::new).toList());
  }

}
