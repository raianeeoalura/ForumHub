package br.alura.ForumHub.application.usecase.topic;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.alura.ForumHub.application.exception.ResourceNotFoundException;
import br.alura.ForumHub.domain.entity.Answer;
import br.alura.ForumHub.domain.repository.AnswerRepository;
import br.alura.ForumHub.domain.repository.TopicRepository;
import br.alura.ForumHub.domain.valueobject.TopicWithAnswers;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTopicWithAnswersUseCase {
  public final static int DEFAULT_PAGE = 1;
  public final static int DEFAULT_SIZE = 10;

  private final TopicRepository topicRepository;
  private final AnswerRepository answerRepository;

  public record GetTopicWithAnswersRequest(String topicId, int page, int size) {
  }

  public TopicWithAnswers execute(String strTopicId) {
    var topicId = UUID.fromString(strTopicId);
    var topic = topicRepository.findById(topicId)
        .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));

    topic.incrementViewCount();
    topicRepository.save(topic);

    List<Answer> answers = answerRepository.findManyByTopicId(topicId, DEFAULT_PAGE, DEFAULT_SIZE);

    return new TopicWithAnswers(topic, answers);
  }

  public TopicWithAnswers execute(GetTopicWithAnswersRequest request) {
    var topicId = UUID.fromString(request.topicId());
    var topic = topicRepository.findById(topicId)
        .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));

    topic.incrementViewCount();
    topicRepository.save(topic);

    List<Answer> answers = answerRepository.findManyByTopicId(topicId, request.page(), request.size());

    return new TopicWithAnswers(topic, answers);
  }
}
