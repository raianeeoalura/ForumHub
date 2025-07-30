package br.alura.ForumHub.application.exception;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String identifier) {
    super("User with " + identifier + " already exists");
  }

  public UserAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }

}
