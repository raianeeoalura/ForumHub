package br.alura.ForumHub.application.exception;

public class TopicNotFoundException extends RuntimeException {

  public TopicNotFoundException() {
    super("Topic not found.");
  }

  public TopicNotFoundException(String message) {
    super(message);
  }

  public TopicNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
