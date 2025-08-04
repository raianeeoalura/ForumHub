package br.alura.ForumHub.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.alura.ForumHub.domain.entity.Topic;

public interface TopicRepository {

  Topic create(Topic topic);

  Topic update(Topic topic);

  Optional<Topic> findById(UUID id);

  Optional<Topic> findBySlug(String slug);

  List<Topic> findMany(int page, int size);

  void delete(UUID topicId);
}
