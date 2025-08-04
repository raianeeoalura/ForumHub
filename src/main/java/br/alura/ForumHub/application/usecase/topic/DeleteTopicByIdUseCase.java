package br.alura.ForumHub.application.usecase.topic;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.alura.ForumHub.application.exception.InactiveResourceException;
import br.alura.ForumHub.application.exception.NotAllowedResourceException;
import br.alura.ForumHub.application.exception.ResourceNotFoundException;
import br.alura.ForumHub.application.exception.UserNotFoundException;
import br.alura.ForumHub.domain.repository.TopicRepository;
import br.alura.ForumHub.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteTopicByIdUseCase {

  private final TopicRepository topicRepository;
  private final UserRepository userRepository;

  public static record DeleteTopicRequest(
      String topicId,
      String authorId) {
  }

  public void execute(DeleteTopicRequest request) {
    var topicId = UUID.fromString(request.topicId());
    var authorId = UUID.fromString(request.authorId());

    var topic = topicRepository.findById(topicId)
        .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

    if (!topic.getAuthorId().equals(authorId)) {
      throw new NotAllowedResourceException("You are not allowed to update this topic.");
    }

    var author = userRepository.findById(authorId)
        .orElseThrow(UserNotFoundException::new);

    if (!author.isActive()) {
      throw new InactiveResourceException(author.getUsername().getValue());
    }

    if (!topic.getIsActive()) {
      throw new InactiveResourceException(topic.getSlug().getValue());
    }

    topicRepository.delete(topicId);
  }
}
