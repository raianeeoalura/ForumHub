package br.alura.ForumHub.application.usecase.topic;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.alura.ForumHub.application.exception.InactiveResourceException;
import br.alura.ForumHub.application.exception.UserNotFoundException;
import br.alura.ForumHub.domain.entity.Topic;
import br.alura.ForumHub.domain.repository.TopicRepository;
import br.alura.ForumHub.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTopicUseCase {

  private final TopicRepository topicRepository;
  private final UserRepository userRepository;

  public record CreateTopicRequest(
      String title,
      String content,
      String authorId) {
  }

  public Topic execute(CreateTopicRequest request) {
    var authorId = UUID.fromString(request.authorId);
    var author = userRepository.findById(authorId)
        .orElseThrow(UserNotFoundException::new);

    if (!author.isActive()) {
      throw new InactiveResourceException(author.getUsername().getValue());
    }

    Topic topic = new Topic(request.title, request.content, author.getId());
    return topicRepository.save(topic);
  }
}
