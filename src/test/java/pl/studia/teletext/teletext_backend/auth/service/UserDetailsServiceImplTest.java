package pl.studia.teletext.teletext_backend.auth.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.studia.teletext.teletext_backend.auth.domain.User;
import pl.studia.teletext.teletext_backend.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserDetailsServiceImpl userDetailsService;

  @Test
  void shouldLoadUserByUsernameSuccessfully() {
    // given
    var username = "test-user";
    var user = new User();
    user.setDeletedAt(null);
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    // when
    var result = userDetailsService.loadUserByUsername(username);

    // then
    assertNotNull(result);
  }

  @Test
  void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
    // given
    var username = "non-existent-user";
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
  }

  @Test
  void shouldThrowUsernameNotFoundExceptionWhenUserIsNotActive() {
    // given
    var username = "non-existent-user";
    var user = new User();
    user.setDeletedAt(Timestamp.from(Instant.now()));
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    // when & then
    assertThrows(
        UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
  }
}
