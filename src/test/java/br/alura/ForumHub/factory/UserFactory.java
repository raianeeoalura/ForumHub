package br.alura.ForumHub.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import br.alura.ForumHub.domain.entity.User;
import br.alura.ForumHub.infra.persistence.repository.UserRepositoryImpl;

@Component
public class UserFactory {

  @Autowired
  private UserRepositoryImpl userRepository;

  private static Faker faker = new Faker();

  public static User makeUser(String name, String username, String email, String password) {
    User user = new User(name, username, email, password);
    return user;
  }

  public static User makeUser() {
    var name = faker.name().fullName();
    var username = faker.lorem().characters(15);
    var email = faker.internet().emailAddress();
    var password = faker.internet().password();

    User user = new User(name, username, email, password);
    return user;
  }

  public User persisteUser() {
    User user = makeUser();
    return userRepository.save(user);
  }
}
