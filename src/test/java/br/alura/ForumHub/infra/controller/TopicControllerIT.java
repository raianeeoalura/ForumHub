package br.alura.ForumHub.infra.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import br.alura.ForumHub.factory.AnswerFactory;
import br.alura.ForumHub.factory.TopicFactory;
import br.alura.ForumHub.factory.UserFactory;
import br.alura.ForumHub.infra.persistence.repository.AnswerRepositoryImpl;
import br.alura.ForumHub.infra.persistence.repository.TopicRepositoryImpl;
import br.alura.ForumHub.infra.persistence.repository.UserRepositoryImpl;

@SpringBootTest
@AutoConfigureMockMvc
class TopicControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TopicRepositoryImpl topicRepository;

  @Autowired
  private UserRepositoryImpl userRepository;

  @Autowired
  private AnswerRepositoryImpl answerRepository;

  @Autowired
  private UserFactory userFactory;

  @Autowired
  private TopicFactory topicFactory;

  @Autowired
  private AnswerFactory answerFactory;

  @BeforeEach
  void deleteAllData() {
    answerRepository.deleteAll();
    topicRepository.deleteAll();
    userRepository.deleteAll();
  }

  // TODO: create integration test for [POST] /topics
  // @Test
  void createTopic() {
  }

  @Test
  @DisplayName("Should list topics with pagination")
  void listTopics() throws Exception {
    var user1 = userFactory.persisteUser();
    var user2 = userFactory.persisteUser();

    topicFactory.persisteTopic(user1.getId());
    topicFactory.persisteTopic(user1.getId());
    topicFactory.persisteTopic(user2.getId());

    mockMvc.perform(get("/topics"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));

    mockMvc.perform(get("/topics?size=2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));

    mockMvc.perform(get("/topics?page=1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @DisplayName("Should increment view count when topic is searched")
  void updateTopicWhenSearched() throws Exception {
    var user = userFactory.persisteUser();
    var topic = topicFactory.persisteTopic(user.getId());

    mockMvc.perform(get("/topics/{id}", topic.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.viewCount", is(1)));

    mockMvc.perform(get("/topics/{id}", topic.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.viewCount", is(2)));

    mockMvc.perform(get("/topics/{id}", topic.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.viewCount", is(3)));
  }

  @Test
  @DisplayName("Should get a topic with its answers")
  void getTopicWithAnswers() throws Exception {
    var user = userFactory.persisteUser();
    var topic = topicFactory.persisteTopic(user.getId());

    answerFactory.persisteAnswer(user.getId(), topic.getId());
    answerFactory.persisteAnswer(user.getId(), topic.getId());

    mockMvc.perform(get("/topics/{id}", topic.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(topic.getId().toString()))
        .andExpect(jsonPath("$.answers", hasSize(2)));
  }

  @Test
  @DisplayName("Should get a topic with its answers pageable")
  void getTopicWithAnswersPageable() throws Exception {
    var user = userFactory.persisteUser();
    var topic = topicFactory.persisteTopic(user.getId());

    for (int i = 0; i < 22; i++) {
      answerFactory.persisteAnswer(user.getId(), topic.getId());
    }

    mockMvc.perform(get("/topics/{id}?size=20", topic.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(topic.getId().toString()))
        .andExpect(jsonPath("$.answers", hasSize(20)));

    mockMvc.perform(get("/topics/{id}?page=1&size=20", topic.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(topic.getId().toString()))
        .andExpect(jsonPath("$.answers", hasSize(2)));
  }
}
