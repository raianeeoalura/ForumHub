package br.alura.ForumHub;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.alura.ForumHub.application.exception.UserAlreadyExistsException;
import br.alura.ForumHub.application.usecase.user.CreateUserUseCase;
import br.alura.ForumHub.application.usecase.user.CreateUserUseCase.CreateUserRequest;
import br.alura.ForumHub.infra.persistence.repository.UserRepositoryImpl;

@SpringBootTest
public class CreateUserUseCaseTest {
  private final static CreateUserRequest userData = new CreateUserRequest(
      "John Doe", "johndoe", "johndoe@test.com", "123456");

  @Autowired
  private CreateUserUseCase createUserUseCase;

  @Autowired
  private UserRepositoryImpl userRepository;

  @BeforeEach
  void clearDatabase() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("Should create a user successfully")
  void testCreateUser() {
    var userCreated = createUserUseCase.execute(userData);

    assertThat(userCreated).isNotNull();
    assertThat(userCreated.getName()).isEqualTo("John Doe");
    assertThat(userCreated.getPassword()).isNotEqualTo(userData.password());
  }

  @Test
  @DisplayName("Should throw an exception when creating a user with an existing email")
  void testCreateUserWithExistingEmail() {
    createUserUseCase.execute(userData);

    var exception = assertThrows(
        UserAlreadyExistsException.class,
        () -> createUserUseCase.execute(userData));

    assertThat(exception.getMessage()).contains(userData.email());
  }

  void testCreateUserWithExistingUsername() {
    createUserUseCase.execute(userData);

    var exception = assertThrows(
        UserAlreadyExistsException.class,
        () -> createUserUseCase.execute(userData));

    assertThat(exception.getMessage()).contains(userData.username());
  }
}
