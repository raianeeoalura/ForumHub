package br.alura.ForumHub.infra.controller;

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

import br.alura.ForumHub.factory.UserFactory;
import br.alura.ForumHub.infra.dto.AuthenticateWithCredentialsRequest;
import br.alura.ForumHub.infra.persistence.repository.UserRepositoryImpl;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class AuthControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JacksonTester<AuthenticateWithCredentialsRequest> authenticateWithCredentialsRequestJson;

  @Autowired
  UserRepositoryImpl userRepository;

  @Autowired
  private UserFactory userFactory;

  @BeforeEach
  void deleteAll() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("[POST /auth] Should return 200 OK and a JWT token when credentials are valid")
  void testAuthenticateUserWithCredentials() throws Exception {
    var user = userFactory.persisteUser("John Doe", "johndoe", "johndoe@example.com", "123456");

    var request = new AuthenticateWithCredentialsRequest(
        user.getUsername().getValue(),
        "123456");
    String requestJson = authenticateWithCredentialsRequestJson.write(request).getJson();

    mockMvc.perform(
        post("/auth")
            .contentType("application/json")
            .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists());
  }
}
