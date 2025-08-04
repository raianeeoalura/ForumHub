package br.alura.ForumHub.infra.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;

import br.alura.ForumHub.factory.TopicFactory;
import br.alura.ForumHub.factory.UserFactory;
import br.alura.ForumHub.infra.dto.CreateAnswerRequestDTO;
import br.alura.ForumHub.infra.persistence.repository.AnswerRepositoryImpl;
import br.alura.ForumHub.infra.persistence.repository.TopicRepositoryImpl;
import br.alura.ForumHub.infra.persistence.repository.UserRepositoryImpl;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AnswerControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JacksonTester<CreateAnswerRequestDTO> createAnswerRequestJson;

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

  @BeforeEach
  void deleteAllData() {
    answerRepository.deleteAll();
    topicRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("Should create an answer for a topic")
  void testCreateAnswer() throws Exception {
    var user = userFactory.persisteUser();
    var topic = topicFactory.persisteTopic(user.getId());

    var request = new CreateAnswerRequestDTO("This is a new answer", topic.getId().toString(), user.getId().toString());
    var json = createAnswerRequestJson.write(request).getJson();

    mockMvc.perform(
        post("/answers")
            .contentType("application/json")
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content", is(request.content())))
        .andExpect(jsonPath("$.topicId", is(request.topicId().toString())))
        .andExpect(jsonPath("$.authorId", is(request.authorId().toString())));
  }
}
