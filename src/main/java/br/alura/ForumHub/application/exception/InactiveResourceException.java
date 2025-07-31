package br.alura.ForumHub.application.exception;

public class InactiveResourceException extends RuntimeException {

  public InactiveResourceException(String identifier) {
    super("Resource (" + identifier + ") is inactive");
  }

  public InactiveResourceException(String message, Throwable cause) {
    super(message, cause);
  }

}
