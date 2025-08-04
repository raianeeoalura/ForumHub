package br.alura.ForumHub.factory;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import br.alura.ForumHub.domain.entity.Answer;
import br.alura.ForumHub.infra.persistence.repository.AnswerRepositoryImpl;

@Component
public class AnswerFactory {

  @Autowired
  private AnswerRepositoryImpl answerRepository;

  private static Faker faker = new Faker();

  public static Answer makeAnswer(String content, UUID topicId, UUID authorId) {
    Answer answer = new Answer(content, authorId, topicId);
    return answer;
  }

  public static Answer makeAnswer() {
    var content = faker.lorem().paragraph();
    var topicId = UUID.randomUUID();
    var authorId = UUID.randomUUID();

    Answer answer = new Answer(content, authorId, topicId);
    return answer;
  }

  public Answer persisteAnswer() {
    Answer answer = makeAnswer();
    answerRepository.save(answer);
    return answer;
  }

  public Answer persisteAnswer(UUID authorId, UUID topicId) {
    Answer answer = makeAnswer();
    answer.setAuthorId(authorId);
    answer.setTopicId(topicId);
    answerRepository.save(answer);
    return answer;
  }
}
