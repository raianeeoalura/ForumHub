package br.alura.ForumHub.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.alura.ForumHub.domain.entity.Answer;

public interface AnswerRepository {

  Answer save(Answer answer);

  Optional<Answer> findById(UUID id);

  List<Answer> findManyByTopicId(UUID topicId, int page, int size);
}
