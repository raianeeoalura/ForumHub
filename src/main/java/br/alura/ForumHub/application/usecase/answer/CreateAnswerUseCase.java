package br.alura.ForumHub.application.usecase.answer;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.alura.ForumHub.application.exception.InactiveResourceException;
import br.alura.ForumHub.application.exception.ResourceNotFoundException;
import br.alura.ForumHub.application.exception.UserNotFoundException;
import br.alura.ForumHub.domain.entity.Answer;
import br.alura.ForumHub.domain.entity.Topic;
import br.alura.ForumHub.domain.entity.User;
import br.alura.ForumHub.domain.repository.AnswerRepository;
import br.alura.ForumHub.domain.repository.TopicRepository;
import br.alura.ForumHub.domain.repository.UserRepository;

@Service
public class CreateAnswerUseCase {

  private final AnswerRepository answerRepository;
  private final TopicRepository topicRepository;
  private final UserRepository userRepository;

  public CreateAnswerUseCase(AnswerRepository answerRepository, TopicRepository topicRepository,
      UserRepository userRepository) {
    this.answerRepository = answerRepository;
    this.topicRepository = topicRepository;
    this.userRepository = userRepository;
  }

  public record CreateAnswerRequest(
      String content,
      String topicId,
      String authorId) {
  }

  public Answer execute(CreateAnswerRequest request) {
    User author = userRepository.findById(UUID.fromString(request.authorId))
        .orElseThrow(UserNotFoundException::new);

    Topic topic = topicRepository.findById(UUID.fromString(request.topicId))
        .orElseThrow(ResourceNotFoundException::new);

    if (!author.isActive()) {
      throw new InactiveResourceException(author.getUsername().getValue());
    }

    if (!topic.getIsActive()) {
      throw new InactiveResourceException(topic.getTitle());
    }

    Answer answer = new Answer(request.content, author.getId(), topic.getId());
    return answerRepository.save(answer);
  }
}
