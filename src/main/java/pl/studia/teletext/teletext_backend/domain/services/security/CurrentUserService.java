package pl.studia.teletext.teletext_backend.domain.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.domain.models.security.User;
import pl.studia.teletext.teletext_backend.domain.repositories.UserRepository;
import pl.studia.teletext.teletext_backend.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

  private final UserRepository userRepository;

  public User getCurrentUser() {
    var auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
      throw new AccessDeniedException("Użytkownik nie jest zalogowany");
    }

    var name = auth.getName();
    return userRepository
        .findByUsername(name)
        .orElseThrow(
            () -> new UserNotFoundException("Użytkownik " + name + " nie został znaleziony"));
  }

  public Long getCurrentUserId() {
    return getCurrentUser().getId();
  }
}
