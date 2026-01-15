package pl.studia.teletext.teletext_backend.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.studia.teletext.teletext_backend.auth.domain.User;
import pl.studia.teletext.teletext_backend.auth.repository.UserRepository;
import pl.studia.teletext.teletext_backend.common.exception.not_found.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class CurrentUserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private CurrentUserService currentUserService;

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldReturnCurrentUserWhenAuthenticated() {
    // given
    var username = "testUser";
    var user = user(1L, username);
    var auth = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
    when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));

    // when
    var result = currentUserService.getCurrentUser();

    // then
    assertNotNull(result);
    assertEquals(username, result.getUsername());
    verify(userRepository, times(1)).findByUsername(eq(username));
  }

  @Test
  void shouldThrowAccessDeniedWhenAuthenticationIsNull() {
    // given
    SecurityContextHolder.clearContext();

    // when & then
    assertThrows(AccessDeniedException.class, () -> currentUserService.getCurrentUser());
    verifyNoInteractions(userRepository);
  }

  @Test
  void shouldThrowAccessDeniedWhenUserIsAnonymous() {
    // given
    var auth = new UsernamePasswordAuthenticationToken("anonymousUser", null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);

    // when & then
    assertThrows(AccessDeniedException.class, () -> currentUserService.getCurrentUser());
    verifyNoInteractions(userRepository);
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenUserDoesNotExistInDatabase() {
    // given
    var username = "missingUser";
    var auth = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
    when(userRepository.findByUsername(eq(username))).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> currentUserService.getCurrentUser());
    verify(userRepository, times(1)).findByUsername(eq(username));
  }

  @Test
  void shouldReturnCurrentUserId() {
    // given
    var id = 10L;
    var user = user(id, "user");
    var auth = new UsernamePasswordAuthenticationToken("user", null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
    when(userRepository.findByUsername(eq("user"))).thenReturn(Optional.of(user));

    // when
    var result = currentUserService.getCurrentUserId();

    // then
    assertEquals(id, result);
  }

  @Test
  void shouldReturnCurrentUsername() {
    // given
    var username = "john";
    var user = user(5L, username);
    var auth = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
    when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));

    // when
    var result = currentUserService.getCurrentUsername();

    // then
    assertEquals(username, result);
  }

  private User user(Long id, String username) {
    var user = new User();
    user.setId(id);
    user.setUsername(username);
    return user;
  }
}
