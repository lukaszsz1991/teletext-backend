package pl.studia.teletext.teletext_backend.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.studia.teletext.teletext_backend.user.api.dto.PasswordChange;

public class PasswordMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value instanceof PasswordChange changeReq) {
      return changeReq.password().equals(changeReq.repeatPassword());
    }
    return true;
  }
}
