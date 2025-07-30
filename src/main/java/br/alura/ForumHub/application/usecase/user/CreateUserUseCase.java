package br.alura.ForumHub.application.usecase.user;

import org.springframework.stereotype.Service;

import br.alura.ForumHub.application.cryptography.HashGenerator;
import br.alura.ForumHub.application.exception.UserAlreadyExistsException;
import br.alura.ForumHub.domain.entity.User;
import br.alura.ForumHub.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

  private final UserRepository userRepository;
  private final HashGenerator hashGenerator;

  public static record CreateUserRequest(
      String name,
      String username,
      String email,
      String password) {
  }

  public User execute(CreateUserRequest request) {
    var userExistsWithEmail = userRepository.findByEmail(request.email());
    if (userExistsWithEmail.isPresent()) {
      throw new UserAlreadyExistsException(request.email());
    }

    var userExistsWithUsername = userRepository.findByUsername(request.username());
    if (userExistsWithUsername.isPresent()) {
      throw new UserAlreadyExistsException(request.username());
    }

    var hashedPassword = hashGenerator.hash(request.password());

    User user = new User(request.name(), request.username(), request.email(), hashedPassword);
    return userRepository.save(user);
  }
}
