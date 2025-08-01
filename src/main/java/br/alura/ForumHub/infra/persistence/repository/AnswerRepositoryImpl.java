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

import br.alura.ForumHub.application.exception.ResourceNotFoundException;
import br.alura.ForumHub.application.exception.UserNotFoundException;
import br.alura.ForumHub.domain.entity.Answer;
import br.alura.ForumHub.domain.repository.AnswerRepository;
import br.alura.ForumHub.infra.persistence.entity.AnswerDB;
import jakarta.transaction.Transactional;

interface AnswerJpaRepository extends JpaRepository<AnswerDB, UUID> {
  List<AnswerDB> findByTopicId(UUID topicId, Pageable pageable);

  List<AnswerDB> findByAuthorId(UUID authorId);
}

@Repository
public class AnswerRepositoryImpl implements AnswerRepository {
  @Autowired
  private AnswerJpaRepository answerJpaRepository;

  @Autowired
  private UserJpaRepository userJpaRepository;

  @Autowired
  private TopicJpaRepository topicJpaRepository;

  public void deleteAll() {
    answerJpaRepository.deleteAll();
  }

  @Override
  @Transactional
  public Answer save(Answer answer) {
    var user = userJpaRepository.findById(answer.getAuthorId()).orElseThrow(UserNotFoundException::new);
    var topic = topicJpaRepository.findById(answer.getTopicId()).orElseThrow(ResourceNotFoundException::new);

    AnswerDB dbAnswer = new AnswerDB(answer);
    dbAnswer.setId(null);
    dbAnswer.setAuthor(user);
    dbAnswer.setTopic(topic);

    var savedAnswer = answerJpaRepository.save(dbAnswer);
    return toDomain(savedAnswer);
  }

  @Override
  public Optional<Answer> findById(UUID id) {
    var dbAnswer = answerJpaRepository.findById(id);
    return dbAnswer.map(this::toDomain);
  }

  @Override
  public List<Answer> findManyByTopicId(UUID topicId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    List<AnswerDB> dbAnswers = answerJpaRepository.findByTopicId(topicId, pageable);

    return dbAnswers.stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
  }

  private Answer toDomain(AnswerDB dbAnswer) {
    Answer answer = new Answer(
        dbAnswer.getId(),
        dbAnswer.getContent(),
        dbAnswer.getAuthor().getId(),
        dbAnswer.getTopic().getId(),
        dbAnswer.getViewCount(),
        dbAnswer.getCreatedAt());

    return answer;
  }
}
