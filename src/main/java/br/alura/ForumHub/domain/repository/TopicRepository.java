package br.alura.ForumHub.domain.repository;

import java.util.Optional;
import java.util.UUID;

import br.alura.ForumHub.domain.entity.Topic;

public interface TopicRepository {

  Topic save(Topic topic);

  Optional<Topic> findById(UUID id);

  Optional<Topic> findBySlug(String slug);
}
