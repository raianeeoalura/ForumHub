package br.alura.ForumHub.factory;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import br.alura.ForumHub.domain.entity.Topic;
import br.alura.ForumHub.infra.persistence.repository.TopicRepositoryImpl;

@Component
public class TopicFactory {

  @Autowired
  private TopicRepositoryImpl topicRepository;

  private static Faker faker = new Faker();

  public static Topic makeTopic(String title, String content, UUID authorId) {
    Topic topic = new Topic(title, content, authorId);
    return topic;
  }

  public static Topic makeTopic() {
    var title = faker.lorem().sentence();
    var content = faker.lorem().paragraph();

    Topic topic = new Topic(title, content, UUID.randomUUID());
    return topic;
  }

  public Topic persisteTopic() {
    Topic topic = makeTopic();
    topicRepository.create(topic);
    return topic;
  }

  public Topic persisteTopic(UUID authorId) {
    Topic topic = makeTopic();
    topic.setAuthorId(authorId);
    return topicRepository.create(topic);
  }
}
