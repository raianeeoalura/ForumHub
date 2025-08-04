package br.alura.ForumHub.application.usecase.topic;

import java.util.List;

import br.alura.ForumHub.domain.entity.Topic;
import org.springframework.stereotype.Service;

import br.alura.ForumHub.domain.repository.TopicRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTopicsUseCase {
  public final static int DEFAULT_PAGE = 0;
  public final static int DEFAULT_SIZE = 10;

  private final TopicRepository topicRepository;

    public static record GetTopicsUseCaseRequest(int page, int size) {
  }

  public List<Topic> execute() {
    return topicRepository.findMany(DEFAULT_PAGE, DEFAULT_SIZE);
  }

  public List<Topic> execute(GetTopicsUseCaseRequest request) {
    return topicRepository.findMany(request.page(), request.size());
  }
}
