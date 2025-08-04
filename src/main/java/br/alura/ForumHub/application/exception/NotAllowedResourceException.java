package br.alura.ForumHub.application.exception;

public class NotAllowedResourceException extends RuntimeException {

  public NotAllowedResourceException() {
    super("Not allowed.");
  }

  public NotAllowedResourceException(String message) {
    super(message);
  }

  public NotAllowedResourceException(String message, Throwable cause) {
    super(message, cause);
  }

}
