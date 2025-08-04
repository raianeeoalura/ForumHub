package br.alura.ForumHub;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.alura.ForumHub.application.exception.InactiveResourceException;
import br.alura.ForumHub.application.usecase.topic.CreateTopicUseCase;
import br.alura.ForumHub.application.usecase.topic.CreateTopicUseCase.CreateTopicRequest;
import br.alura.ForumHub.domain.entity.User;
import br.alura.ForumHub.infra.persistence.repository.TopicRepositoryImpl;
import br.alura.ForumHub.infra.persistence.repository.UserRepositoryImpl;

@SpringBootTest
public class CreateTopicUseCaseTest {
  @Autowired
  private CreateTopicUseCase createTopicUseCase;

  @Autowired
  private UserRepositoryImpl userRepository;

  @Autowired
  private TopicRepositoryImpl topicRepository;

  @BeforeEach
  void clearDatabase() {
    topicRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("Should create a topic successfully")
  void testCreateTopic() {
    var user = createUser();

    var topicData = new CreateTopicRequest(
        "Topic test",
        "This is a test topic content",
        user.getId().toString());

    var topicCreated = createTopicUseCase.execute(topicData);

    assertThat(topicCreated).isNotNull();
    assertThat(topicCreated.getId()).isNotNull();
    assertThat(topicCreated.getTitle()).isEqualTo(topicData.title());
    assertThat(topicCreated.getAuthorId()).isEqualTo(user.getId());
  }

  @Test
  @DisplayName("Should not create a topic with a duplicated slug")
  void testCreateDuplicateTopic() {
    var user = createUser();

    var topicData = new CreateTopicRequest(
        "Topic test",
        "This is a test topic content",
        user.getId().toString());

    var topicCreated1 = createTopicUseCase.execute(topicData);
    assertThat(topicCreated1).isNotNull();

    var topicCreated2 = createTopicUseCase.execute(topicData);
    assertThat(topicCreated2.getSlug().toString()).isNotEqualTo(topicCreated1.getSlug().toString());
  }

  @Test
  @DisplayName("Should not create a topic with a inactive user")
  void testCreateTopicWithInactiveUser() {
    var domainUser = new User("John Doe", "johndoe", "johndoe@example.com", "123456");
    domainUser.deactivate();
    var user = userRepository.save(domainUser);

    var topicData = new CreateTopicRequest(
        "Topic test",
        "This is a test topic content",
        user.getId().toString());

    var exception = assertThrows(InactiveResourceException.class, () -> createTopicUseCase.execute(topicData));
    assertThat(exception.getMessage()).contains(user.getUsername().getValue());
  }

  private User createUser() {
    var user = userRepository.save(new User("John Doe", "johndoe", "johndoe@example.com", "123456"));
    return user;
  }
}
