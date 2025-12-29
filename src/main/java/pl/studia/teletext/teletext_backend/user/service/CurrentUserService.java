package pl.studia.teletext.teletext_backend.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.auth.domain.User;
import pl.studia.teletext.teletext_backend.auth.repository.UserRepository;
import pl.studia.teletext.teletext_backend.common.exception.not_found.UserNotFoundException;

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

  public String getCurrentUsername() {
    return getCurrentUser().getUsername();
  }
}
