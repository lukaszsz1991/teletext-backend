package pl.studia.teletext.teletext_backend.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.studia.teletext.teletext_backend.auth.domain.User;
import pl.studia.teletext.teletext_backend.auth.repository.UserRepository;
import pl.studia.teletext.teletext_backend.common.exception.not_found.UserNotFoundException;
import pl.studia.teletext.teletext_backend.events.mailing.AccountCreatedEmailEvent;
import pl.studia.teletext.teletext_backend.events.mailing.AccountStatusChangedEmailEvent;
import pl.studia.teletext.teletext_backend.events.mailing.PasswordChangedEmailEvent;
import pl.studia.teletext.teletext_backend.user.api.dto.ChangeUserPasswordRequest;
import pl.studia.teletext.teletext_backend.user.api.dto.CreateUserRequest;
import pl.studia.teletext.teletext_backend.user.api.dto.UpdateUserRequest;
import pl.studia.teletext.teletext_backend.user.api.dto.UserResponse;
import pl.studia.teletext.teletext_backend.user.mapper.UserMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private CurrentUserService currentUserService;
  @Mock private ApplicationEventPublisher eventPublisher;
  @InjectMocks private UserService userService;

  @Test
  void shouldReturnAllUsersIncludeDeleted() {
    // given
    var includeDeleted = true;
    var users = List.of(user(false), user(true));
    when(userRepository.findAll()).thenReturn(users);
    when(userMapper.toUserResponse(any(User.class))).thenReturn(mock(UserResponse.class));

    // when
    var result = userService.getAllUsers(includeDeleted);

    // then
    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  void shouldReturnAllUsersWithoutDeleted() {
    // given
    var includeDeleted = false;
    var users = List.of(user(false), user(true));
    when(userRepository.findAll()).thenReturn(users);
    when(userMapper.toUserResponse(any(User.class))).thenReturn(mock(UserResponse.class));

    // when
    var result = userService.getAllUsers(includeDeleted);

    // then
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void shouldReturnEmptyListWhenAllUsersAreDeleted() {
    // given
    var includeDeleted = false;
    var users = List.of(user(true), user(true));
    when(userRepository.findAll()).thenReturn(users);

    // when
    var result = userService.getAllUsers(includeDeleted);

    // then
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnUserById() {
    // given
    var id = 1L;
    var user = user(false);
    when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
    when(userMapper.toUserResponse(eq(user))).thenReturn(mock(UserResponse.class));

    // when
    var result = userService.getUserById(id);

    // then
    assertNotNull(result);
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenUserIsDeleted_ById() {
    // given
    var id = 1L;
    var user = user(true);
    when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenUserIsNotPresentById() {
    // given
    var id = 1L;
    when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
  }

  @Test
  void shouldCreateUser() {
    // given
    var createRequest = new CreateUserRequest("test-username", "test-email", "test-password", "test-password");
    var mappedUser = user(false);
    when(userMapper.toUserEntity(eq(createRequest))).thenReturn(mappedUser);
    when(passwordEncoder.encode(eq(createRequest.password()))).thenReturn("encoded-password");
    when(userRepository.save(eq(mappedUser))).thenReturn(mappedUser);
    when(userMapper.toUserResponse(eq(mappedUser))).thenReturn(mock(UserResponse.class));

    // when
    var result = userService.createUser(createRequest);

    // then
    assertNotNull(result);
    verify(userRepository, times(1)).save(eq(mappedUser));
    verify(eventPublisher, times(1)).publishEvent(any(AccountCreatedEmailEvent.class));
  }

  @Test
  void shouldUpdateUser() {
    // given
    var id = 1L;
    var updateRequest = new UpdateUserRequest("updated-username", "updated-email");
    var existingUser = user(false);
    when(userRepository.findById(eq(id))).thenReturn(Optional.of(existingUser));
    when(userRepository.save(eq(existingUser))).thenReturn(existingUser);
    when(userMapper.toUserResponse(eq(existingUser))).thenReturn(mock(UserResponse.class));

    // when
    var result = userService.updateUser(id, updateRequest);

    // then
    assertNotNull(result);
    verify(userRepository, times(1)).save(existingUser);
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenUpdatedUserDoesNotExist() {
    // given
    var id = 1L;
    var updateRequest = new UpdateUserRequest("updated-username", "updated-email");
    when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.updateUser(id, updateRequest));
  }

  @Test
  void shouldChangePassword() {
    // given
    var id = 1L;
    var password = "new-password";
    var changePasswordRequest = new ChangeUserPasswordRequest(password, password);
    var user = user(false);
    when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
    when(passwordEncoder.encode(eq(password))).thenReturn("encoded-password");
    when(userRepository.save(eq(user))).thenReturn(user);

    // when
    userService.changePassword(id, changePasswordRequest);

    // then
    verify(passwordEncoder, times(1)).encode(eq(password));
    verify(userRepository, times(1)).save(eq(user));
    verify(eventPublisher, times(1)).publishEvent(any(PasswordChangedEmailEvent.class));
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenUserToChangePasswordIsNotPresent() {
    // given
    var id = 1L;
    var password = "new-password";
    var changePasswordRequest = new ChangeUserPasswordRequest(password, password);
    when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.changePassword(id, changePasswordRequest));
  }

  @Test
  void shouldDeleteUserById() {
    // given
    var id = 1L;
    var user = user(false);
    when(currentUserService.getCurrentUserId()).thenReturn(2L);
    when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
    when(userRepository.save(eq(user))).thenReturn(user(true));

    // when
    userService.deleteUserById(id);

    // then
    verify(userRepository, times(1)).save(eq(user));
    verify(eventPublisher, times(1)).publishEvent(any(AccountStatusChangedEmailEvent.class));
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenDeletedUserIsNotPresent() {
    // given
    var id = 1L;
    when(currentUserService.getCurrentUserId()).thenReturn(2L);
    when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(id));
    verify(userRepository, never()).save(any(User.class));
    verify(eventPublisher, never()).publishEvent(any(AccountStatusChangedEmailEvent.class));
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenDeletedUserIsAlreadyDeleted() {
    // given
    var id = 1L;
    var user = user(true);
    when(currentUserService.getCurrentUserId()).thenReturn(2L);
    when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(id));
    verify(userRepository, never()).save(any(User.class));
    verify(eventPublisher, never()).publishEvent(any(AccountStatusChangedEmailEvent.class));
  }

  @Test
  void shouldThrowAccessDeniedExceptionWhenTryingToDeleteOwnAccount() {
    // given
    var id = 1L;
    when(currentUserService.getCurrentUserId()).thenReturn(id);

    // when & then
    assertThrows(AccessDeniedException.class, () -> userService.deleteUserById(id));
    verify(userRepository, never()).save(any(User.class));
    verify(eventPublisher, never()).publishEvent(any(AccountStatusChangedEmailEvent.class));
  }

  @Test
  void shouldRestoreUserById() {
    // given
    var id = 1L;
    var user = user(true);
    when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
    when(userRepository.save(eq(user))).thenReturn(user(false));

    // when
    userService.restoreUserById(id);

    // then
    verify(userRepository, times(1)).save(eq(user));
    verify(eventPublisher, times(1)).publishEvent(any(AccountStatusChangedEmailEvent.class));
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenRestoredUserIsNotPresent() {
    // given
    var id = 1L;
    when(userRepository.findById(eq(id))).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.restoreUserById(id));
    verify(userRepository, never()).save(any(User.class));
    verify(eventPublisher, never()).publishEvent(any(AccountStatusChangedEmailEvent.class));
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenRestoredUserIsNotDeleted() {
    // given
    var id = 1L;
    var user = user(false);
    when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.restoreUserById(id));
    verify(userRepository, never()).save(any(User.class));
    verify(eventPublisher, never()).publishEvent(any(AccountStatusChangedEmailEvent.class));
  }

  @Test
  void shouldReturnUserByUsername() {
    // given
    var username = "test-username";
    var user = user(false);
    when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));

    // when
    var result = userService.getUserByUsername(username);

    // then
    assertNotNull(result);
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenUserIsNotPresent_ByUsername() {
    // given
    var username = "test-username";
    when(userRepository.findByUsername(eq(username))).thenReturn(Optional.empty());

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(username));
  }

  @Test
  void shouldThrowUserNotFoundExceptionWhenUserIsDeleted_ByUsername() {
    // given
    var username = "test-username";
    var user = user(true);
    when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));

    // when & then
    assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(username));
  }

  private User user(boolean isDeleted) {
    var user = new User();
    user.setEmail("test-email@example.com");
    user.setUsername("test-username");
    user.setCreatedAt(Timestamp.from(Instant.now()));
    user.setUpdatedAt(Timestamp.from(Instant.now()));
    if (isDeleted) user.setDeletedAt(Timestamp.from(Instant.now()));
    return user;
  }
}
