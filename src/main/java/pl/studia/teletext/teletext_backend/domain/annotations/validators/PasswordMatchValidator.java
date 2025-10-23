package pl.studia.teletext.teletext_backend.domain.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.studia.teletext.teletext_backend.api.admin.dtos.PasswordChange;
import pl.studia.teletext.teletext_backend.domain.annotations.PasswordsMatch;

public class PasswordMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value instanceof PasswordChange changeReq) {
      return changeReq.password().equals(changeReq.repeatPassword());
    }
    return true;
  }
}
