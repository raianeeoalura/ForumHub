package br.alura.ForumHub.infra.config;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.alura.ForumHub.application.exception.UserAlreadyExistsException;

@RestControllerAdvice
public class RestExceptionHandler {

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
