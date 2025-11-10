package pl.studia.teletext.teletext_backend.api;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.studia.teletext.teletext_backend.exceptions.JwtValidatingException;
import pl.studia.teletext.teletext_backend.exceptions.NotFoundException;
import pl.studia.teletext.teletext_backend.exceptions.ExternalApiException;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ProblemDetail> handleNotFoundException(NotFoundException ex) {
    var status = HttpStatus.NOT_FOUND;
    var problemDetail = ProblemDetail.forStatus(status);
    problemDetail.setDetail(ex.getMessage());
    switch (ex.getClass().getSimpleName()) {
    case "UserNotFoundException" -> problemDetail.setTitle("User Not Found");
    case "PageNotFoundException" -> problemDetail.setTitle("Page Not Found");
    case "CityNotFoundException" -> problemDetail.setTitle("City Not Found");
    default -> problemDetail.setTitle("Resource Not Found");
    }
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(JwtValidatingException.class)
  public ResponseEntity<ProblemDetail> handleJwtValidatingException(JwtValidatingException ex) {
    var status = HttpStatus.UNAUTHORIZED;
    var problemDetail = ProblemDetail.forStatusAndDetail(status, "JWT Validation Error: " + ex.getMessage());
    problemDetail.setTitle("Unauthorized");
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({ UsernameNotFoundException.class, AuthenticationException.class })
  public ResponseEntity<ProblemDetail> handleUsernameNotFoundException(AuthenticationException e) {
    var status = HttpStatus.UNAUTHORIZED;
    var problemDetail = ProblemDetail.forStatusAndDetail(status, e.getMessage());
    problemDetail.setTitle("Authentication Failed");
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ProblemDetail> handleValidationErrors(Exception e) {
    String errorMessage;
    if (e instanceof MethodArgumentNotValidException manve) {
      errorMessage = manve.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
    } else {
      errorMessage = e.getMessage();
    }
    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
    problemDetail.setTitle("Validation Error");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
  }


  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ProblemDetail> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
    var status = HttpStatus.METHOD_NOT_ALLOWED;
    var problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    problemDetail.setTitle("Method Not Allowed");
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ApiResponse(responseCode = "409", description = "Data integrity violation")
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ProblemDetail> handleDataAccessException(DataAccessException e) {
    HttpStatus status;
    String message;
    if (e instanceof DataIntegrityViolationException ie) {
      status = HttpStatus.CONFLICT;
      message = "Data integrity violation: " + ie.getMostSpecificCause().getMessage();
    } else {
      log.error("Database access error", e);
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      message = "Database error: " + e.getMessage();
    }
    var problemDetail = ProblemDetail.forStatusAndDetail(status, message);
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity<ProblemDetail> handleExternalApiException(ExternalApiException ex) {
    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(ex.getStatus()),
      "External API Error: " + ex.getMessage());
    problemDetail.setTitle("API Error");
    return ResponseEntity.status(ex.getStatus()).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleAllExceptions(Exception ex) {
    log.error("Unhandled exception occurred", ex);
    var status = HttpStatus.INTERNAL_SERVER_ERROR;
    var problemDetail = ProblemDetail.forStatusAndDetail(status, "An unexpected error occurred: " + ex.getMessage());
    problemDetail.setTitle("Unexpected Error");
    return ResponseEntity.status(status).body(problemDetail);
  }
}
