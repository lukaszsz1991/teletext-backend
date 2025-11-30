package pl.studia.teletext.teletext_backend.domain.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import pl.studia.teletext.teletext_backend.domain.annotations.validators.PasswordMatchValidator;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
public @interface PasswordsMatch {
  String message() default "Passwords do not match";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
