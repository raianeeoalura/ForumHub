package br.alura.ForumHub.infra.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.alura.ForumHub.domain.entity.Topic;
import br.alura.ForumHub.domain.repository.TopicRepository;
import br.alura.ForumHub.domain.valueobject.Slug;
import br.alura.ForumHub.infra.persistence.entity.TopicDB;
import br.alura.ForumHub.infra.persistence.entity.UserDB;

interface TopicJpaRepository extends JpaRepository<TopicDB, UUID> {
  List<TopicDB> findByAuthorId(UUID userId);

  Optional<TopicDB> findBySlug(String slug);
}

@Repository
public class TopicRepositoryImpl implements TopicRepository {
  @Autowired
  private TopicJpaRepository topicJpaRepository;

  public void deleteAll() {
    topicJpaRepository.deleteAll();
  }

  @Override
  public Topic create(Topic topic) {
    UserDB author = new UserDB(topic.getAuthorId());

    TopicDB entity = new TopicDB(
        null,
        topic.getTitle(),
        topic.getContent(),
        topic.getSlug().getValue(),
        author,
        topic.getViewCount(),
        topic.getCreatedAt(),
        topic.getIsActive(),
        topic.getUpdatedAt(), List.of());

    TopicDB savedTopic = topicJpaRepository.save(entity);
    return toDomain(savedTopic);
  }

  @Override
  public Topic update(Topic topic) {
    UserDB author = new UserDB(topic.getAuthorId());

    TopicDB entity = new TopicDB(
        topic.getId(),
        topic.getTitle(),
        topic.getContent(),
        topic.getSlug().getValue(),
        author,
        topic.getViewCount(),
        topic.getCreatedAt(),
        topic.getIsActive(),
        topic.getUpdatedAt(), List.of());

    TopicDB savedTopic = topicJpaRepository.save(entity);
    return toDomain(savedTopic);
  }

  @Override
  public Optional<Topic> findById(UUID id) {
    return topicJpaRepository.findById(id)
        .map(this::toDomain);
  }

  @Override
  public Optional<Topic> findBySlug(String slug) {
    var topicDatabase = topicJpaRepository.findBySlug(slug).map(this::toDomain);
    return topicDatabase;
  }

  @Override
  public List<Topic> findMany(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    var topicsDB = topicJpaRepository.findAll(pageable);
    return topicsDB.stream().map(this::toDomain).collect(Collectors.toList());
  }

  @Override
  public void delete(UUID topicId) {
    topicJpaRepository.deleteById(topicId);
  }

  private Topic toDomain(TopicDB dbTopic) {
    Topic topic = new Topic(
        dbTopic.getId(),
        dbTopic.getTitle(),
        dbTopic.getContent(),
        dbTopic.getAuthor().getId(),
        new Slug(dbTopic.getSlug()),
        dbTopic.getCreatedAt(),
        dbTopic.getUpdatedAt(),
        dbTopic.isActive(),
        dbTopic.getViewCount());

    return topic;
  }
}
