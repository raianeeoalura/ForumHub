package br.alura.ForumHub.infra.config;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.alura.ForumHub.application.exception.ResourceNotFoundException;
import br.alura.ForumHub.application.exception.UserAlreadyExistsException;
import br.alura.ForumHub.application.exception.UserNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    var response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
    var response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    var response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
    var response = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ValidationErrorResponse>> handleValidationException(MethodArgumentNotValidException ex) {
    var errors = ex.getFieldErrors();

    List<ValidationErrorResponse> response = errors.stream().map(ValidationErrorResponse::new).toList();
    return ResponseEntity.badRequest().body(response);
  }

  private record ValidationErrorResponse(String field, String message) {
    public ValidationErrorResponse(FieldError erro) {
      this(erro.getField(), erro.getDefaultMessage());
    }
  }

  private record ErrorResponse(int status, String message) {
  }
}
