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
import br.alura.ForumHub.factory.TopicFactory;
import br.alura.ForumHub.factory.UserFactory;
import br.alura.ForumHub.infra.persistence.repository.AnswerRepositoryImpl;
import br.alura.ForumHub.infra.persistence.repository.TopicRepositoryImpl;
import br.alura.ForumHub.infra.persistence.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
public class CreateAnswerUseCaseTest {
  @Autowired
  private CreateAnswerUseCase createAnswerUseCase;

  @Autowired
  private UserRepositoryImpl userRepository;

  @Autowired
  private TopicRepositoryImpl topicRepository;

  @Autowired
  private AnswerRepositoryImpl answerRepository;

  @Autowired
  private UserFactory userFactory;

  @Autowired
  private TopicFactory topicFactory;

  @BeforeEach
  void clearDatabase() {
    answerRepository.deleteAll();
    topicRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("Should create an answer successfully")
  void testCreateAnswer() {
    var user = userFactory.persisteUser();
    var topic = topicFactory.persisteTopic(user.getId());

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
    var user = userFactory.persisteUser();
    var topic = topicFactory.persisteTopic(user.getId());

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
    var domainUser = UserFactory.makeUser();
    domainUser.deactivate();
    var user = userRepository.save(domainUser);
    var topic = topicFactory.persisteTopic(user.getId());

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
    var user = userFactory.persisteUser();
    var topic = topicFactory.persisteTopic(user.getId());
    topic.setActive(false);
    topic = topicRepository.update(topic);

    var answerData = new CreateAnswerRequest(
        "This is a test answer content",
        topic.getId().toString(),
        user.getId().toString());

    var exception = assertThrows(InactiveResourceException.class, () -> createAnswerUseCase.execute(answerData));
    assertThat(exception.getMessage()).contains(topic.getTitle());
  }
}
