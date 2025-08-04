package br.alura.ForumHub.infra.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import br.alura.ForumHub.infra.dto.CreateUserWithCredentialsRequest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JacksonTester<CreateUserWithCredentialsRequest> createUserWithCredentialsRequestJson;

  @Test
  @DisplayName("[POST /users] Should create a user successfully")
  void testCreateUser() throws Exception {
    var request = new CreateUserWithCredentialsRequest(
        "Jane Doe",
        "janedoe",
        "johndoe@example.com",
        "123456");
    String requestJson = createUserWithCredentialsRequestJson.write(request).getJson();

    var response = mockMvc.perform(
        post("/users")
            .contentType("application/json")
            .content(requestJson))
        .andReturn()
        .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    assertThat(response.getContentAsString()).contains(request.email());
  }

  @Test
  @DisplayName("[POST /users] Should return 400 Bad Request when request body is invalid")
  void testCreateUserWithInvalidBody() throws Exception {
    var request = new CreateUserWithCredentialsRequest(
        "Jane Doe",
        "",
        "wrong email format",
        "123");

    String requestJson = createUserWithCredentialsRequestJson.write(request).getJson();

    var response = mockMvc.perform(
        post("/users")
            .contentType("application/json")
            .content(requestJson))
        .andReturn()
        .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  @DisplayName("[POST /users] Should return 409 Conflict when user already exists")
  void testCreateUserDuplicated() throws Exception {
    var request = new CreateUserWithCredentialsRequest(
        "Jane Doe",
        "janedoe",
        "johndoe@example.com",
        "123456");

    String requestJson = createUserWithCredentialsRequestJson.write(request).getJson();

    mockMvc.perform(
        post("/users")
            .contentType("application/json")
            .content(requestJson))
        .andReturn()
        .getResponse();

    var response = mockMvc.perform(
        post("/users")
            .contentType("application/json")
            .content(requestJson))
        .andReturn()
        .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
  }
}
