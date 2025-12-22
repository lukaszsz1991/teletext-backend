package pl.studia.teletext.teletext_backend.api;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.studia.teletext.teletext_backend.exceptions.ExternalApiException;
import pl.studia.teletext.teletext_backend.exceptions.IllegalPageNumberException;
import pl.studia.teletext.teletext_backend.exceptions.InvalidJsonConfigException;
import pl.studia.teletext.teletext_backend.exceptions.JwtValidatingException;
import pl.studia.teletext.teletext_backend.exceptions.notfound.NotFoundException;

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
      case "UserNotFoundException" -> problemDetail.setTitle("Użytkownik nie znaleziony");
      case "PageNotFoundException" -> problemDetail.setTitle("Strona telegazety nie znaleziona");
      case "TemplateNotFoundException" -> problemDetail.setTitle("Szablon strony nie znaleziony");
      case "CityNotFoundException" -> problemDetail.setTitle("Miasto nie znalezione");
      case "PageStatsNotFoundException" ->
          problemDetail.setTitle("Statystyki strony nie znalezione");
      default -> problemDetail.setTitle("Nie znaleziono zasobu");
    }
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(JwtValidatingException.class)
  public ResponseEntity<ProblemDetail> handleJwtValidatingException(JwtValidatingException ex) {
    var status = HttpStatus.UNAUTHORIZED;
    var problemDetail =
        ProblemDetail.forStatusAndDetail(status, "Błąd logowania: " + ex.getMessage());
    problemDetail.setTitle("Nieautoryzowany dostęp");
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(InvalidJsonConfigException.class)
  public ResponseEntity<ProblemDetail> handleInvalidJsonConfigException(
      InvalidJsonConfigException ex) {
    var status = HttpStatus.UNPROCESSABLE_ENTITY;
    var problemDetail =
        ProblemDetail.forStatusAndDetail(
            status, "Błąd walidacji konfiguracji szablonu: " + ex.getMessage());
    problemDetail.setTitle("Błąd walidacji");
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(IllegalPageNumberException.class)
  public ResponseEntity<ProblemDetail> handleIllegalPageNumberException(
      IllegalPageNumberException ex) {
    var status = HttpStatus.CONFLICT;
    var problemDetail =
        ProblemDetail.forStatusAndDetail(status, "Błąd numeru strony: " + ex.getMessage());
    problemDetail.setTitle("Konflikt numeru strony");
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({AuthenticationException.class})
  public ResponseEntity<ProblemDetail> handleUsernameNotFoundException(AuthenticationException e) {
    var status = HttpStatus.UNAUTHORIZED;
    var problemDetail = ProblemDetail.forStatusAndDetail(status, e.getMessage());
    problemDetail.setTitle("Uwierzytelnianie nie powiodło się");
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ExceptionHandler({
    MethodArgumentNotValidException.class,
    ValidationException.class,
    IllegalArgumentException.class,
    TypeMismatchException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ProblemDetail> handleValidationErrors(Exception e) {
    String errorMessage = e.getMessage();
    String title = "Błąd walidacji";
    if (e instanceof MethodArgumentNotValidException ex) {
      errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
    } else if (e instanceof IllegalArgumentException || e instanceof TypeMismatchException) {
      title = "Nieprawidłowy argument";
    }
    var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
    problemDetail.setTitle(title);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ProblemDetail> handleMethodNotSupported(
      HttpRequestMethodNotSupportedException ex) {
    var status = HttpStatus.METHOD_NOT_ALLOWED;
    var problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    problemDetail.setTitle("Niedozwolona metoda");
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex) {
    var status = HttpStatus.FORBIDDEN;
    var problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    problemDetail.setTitle("Zakaz dostępu");
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ApiResponse(responseCode = "409", description = "Data integrity violation")
  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ProblemDetail> handleDataAccessException(DataAccessException e) {
    HttpStatus status;
    String message;
    if (e instanceof DataIntegrityViolationException ie) {
      status = HttpStatus.CONFLICT;
      message = "Błąd integralności danych: " + ie.getMostSpecificCause().getMessage();
    } else {
      log.error("Database access error", e);
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      message = "Błąd bazy danych: " + e.getMessage();
    }
    var problemDetail = ProblemDetail.forStatusAndDetail(status, message);
    return ResponseEntity.status(status).body(problemDetail);
  }

  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity<ProblemDetail> handleExternalApiException(ExternalApiException ex) {
    var problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatusCode.valueOf(ex.getStatus()),
            "Błąd zewnętrzenego serwisu: " + ex.getMessage());
    problemDetail.setTitle("Błąd API");
    return ResponseEntity.status(ex.getStatus()).body(problemDetail);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleAllExceptions(Exception ex) {
    log.error("Unhandled exception occurred", ex);
    var status = HttpStatus.INTERNAL_SERVER_ERROR;
    var problemDetail =
        ProblemDetail.forStatusAndDetail(status, "Wystąpił nieoczekiwany błąd: " + ex.getMessage());
    problemDetail.setTitle("Nieoczekiwany błąd serwera");
    return ResponseEntity.status(status).body(problemDetail);
  }
}
