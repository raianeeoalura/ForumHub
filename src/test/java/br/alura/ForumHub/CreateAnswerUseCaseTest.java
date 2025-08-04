package br.alura.ForumHub;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.alura.ForumHub.application.exception.InactiveResourceException;
import br.alura.ForumHub.application.exception.ResourceNotFoundException;
import br.alura.ForumHub.application.exception.UserNotFoundException;
import br.alura.ForumHub.application.usecase.answer.CreateAnswerUseCase;
import br.alura.ForumHub.application.usecase.answer.CreateAnswerUseCase.CreateAnswerRequest;
import br.alura.ForumHub.domain.entity.Topic;
import br.alura.ForumHub.domain.entity.User;
import br.alura.ForumHub.infra.persistence.repository.AnswerRepositoryImpl;
import br.alura.ForumHub.infra.persistence.repository.TopicRepositoryImpl;
import br.alura.ForumHub.infra.persistence.repository.UserRepositoryImpl;

@SpringBootTest
public class CreateAnswerUseCaseTest {
  @Autowired
  private CreateAnswerUseCase createAnswerUseCase;

  @Autowired
  private UserRepositoryImpl userRepository;

  @Autowired
  private TopicRepositoryImpl topicRepository;

  @Autowired
  private AnswerRepositoryImpl answerRepository;

  @BeforeEach
  void clearDatabase() {
    userRepository.deleteAll();
    topicRepository.deleteAll();
    answerRepository.deleteAll();
  }

  @Test
  @DisplayName("Should create an answer successfully")
  void testCreateAnswer() {
    var user = createUser();
    var topic = createTopic(user.getId());

    var answerData = new CreateAnswerRequest(
        "This is a test answer content",
        topic.getId().toString(),
        user.getId().toString());

    var answerCreated = createAnswerUseCase.execute(answerData);

    assertThat(answerCreated).isNotNull();
    assertThat(answerCreated.getId()).isNotNull();
    assertThat(answerCreated.getTopicId()).isEqualTo(topic.getId());
    assertThat(answerCreated.getAuthorId()).isEqualTo(user.getId());
  }

  @Test
  @DisplayName("Should not create an answer with non-existent topic and user")
  void testCreateAnswerWithNonTopicAndUser() {
    var user = createUser();
    var topic = createTopic(user.getId());

    var answerDataWithoutUser = new CreateAnswerRequest(
        "This is a test answer content",
        topic.getId().toString(),
        UUID.randomUUID().toString());

    var answerDataWithoutTopic = new CreateAnswerRequest(
        "This is a test answer content",
        UUID.randomUUID().toString(),
        user.getId().toString());

    assertThrows(UserNotFoundException.class, () -> createAnswerUseCase.execute(answerDataWithoutUser));
    assertThrows(ResourceNotFoundException.class, () -> createAnswerUseCase.execute(answerDataWithoutTopic));

    assertThat(answerRepository.findManyByTopicId(topic.getId(), 0, 10)).isEmpty();
  }

  @Test
  @DisplayName("Should not create a answer with a inactive user")
  void testCreateAnswerWithInactiveUser() {
    var domainUser = new User("John Doe", "johndoe", "johndoe@example.com", "123456");
    domainUser.deactivate();
    var user = userRepository.save(domainUser);
    var topic = createTopic(user.getId());

    var answerData = new CreateAnswerRequest(
        "This is a test answer content",
        topic.getId().toString(),
        user.getId().toString());

    var exception = assertThrows(InactiveResourceException.class, () -> createAnswerUseCase.execute(answerData));
    assertThat(exception.getMessage()).contains(user.getUsername().getValue());
  }

  @Test
  @DisplayName("Should not create a answer with an inactive topic")
  void testCreateAnswerWithInactiveTopic() {
    var user = createUser();
    var domainTopic = new Topic("Test Topic", "This is a test topic content", user.getId());
    domainTopic.setActive(false);
    var topic = topicRepository.create(domainTopic);

    var answerData = new CreateAnswerRequest(
        "This is a test answer content",
        topic.getId().toString(),
        user.getId().toString());

    var exception = assertThrows(InactiveResourceException.class, () -> createAnswerUseCase.execute(answerData));
    assertThat(exception.getMessage()).contains(topic.getTitle());
  }

  private User createUser() {
    var user = userRepository.save(new User("John Doe", "johndoe", "johndoe@example.com", "123456"));
    return user;
  }

  private Topic createTopic(UUID userId) {
    var topic = new Topic("Test Topic", "This is a test topic content", userId);
    return topicRepository.create(topic);
  }
}
