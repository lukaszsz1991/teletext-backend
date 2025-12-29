package pl.studia.teletext.teletext_backend.user.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final CurrentUserService currentUserService;
  private final ApplicationEventPublisher eventPublisher;

  public List<UserResponse> getAllUsers(boolean includeDeleted) {
    var users = userRepository.findAll();
    return users.stream()
        .filter(u -> u.getDeletedAt() == null || includeDeleted)
        .map(mapper::toUserResponse)
        .toList();
  }

  @Cacheable(value = "users", key = "#id", unless = "#result == null")
  public UserResponse getUserById(Long id) {
    var user = getUserEntityById(id);
    return mapper.toUserResponse(user);
  }

  @Transactional
  @CacheEvict(value = "users", key = "#result.id")
  public UserResponse createUser(CreateUserRequest request) {
    var user = mapper.toUserEntity(request);
    user.setPassword(passwordEncoder.encode(request.password()));
    user = userRepository.save(user);
    eventPublisher.publishEvent(
        new AccountCreatedEmailEvent(
            user.getEmail(),
            user.getUsername(),
            user.getCreatedAt().toLocalDateTime(),
            currentUserService.getCurrentUsername()));
    return mapper.toUserResponse(user);
  }

  @Transactional
  @CachePut(value = "users", key = "#id", unless = "#result == null")
  public UserResponse updateUser(Long id, UpdateUserRequest request) {
    var user = getUserEntityById(id);
    user.setUsername(request.username());
    user.setEmail(request.email());
    user = userRepository.save(user);
    return mapper.toUserResponse(user);
  }

  @Transactional
  @CacheEvict(value = "users", key = "#id")
  public void changePassword(Long id, ChangeUserPasswordRequest request) {
    var user = getUserEntityById(id);
    user.setPassword(passwordEncoder.encode(request.password()));
    userRepository.save(user);
    eventPublisher.publishEvent(
        new PasswordChangedEmailEvent(
            user.getEmail(),
            user.getUpdatedAt().toLocalDateTime(),
            currentUserService.getCurrentUsername()));
  }

  @Transactional
  @CacheEvict(value = "users", key = "#id")
  public void deleteUserById(Long id) {
    if (currentUserService.getCurrentUserId().equals(id))
      throw new AccessDeniedException("Nie można usunąć własnego konta");
    var user = getUserEntityById(id);
    user.setDeletedAt(Timestamp.from(Instant.now()));
    userRepository.save(user);
    eventPublisher.publishEvent(
        new AccountStatusChangedEmailEvent(
            user.getEmail(),
            false,
            user.getDeletedAt().toLocalDateTime(),
            currentUserService.getCurrentUsername()));
  }

  @Transactional
  @CachePut(value = "users", key = "#id", unless = "#result == null")
  public UserResponse restoreUserById(Long id) {
    var user = getDeletedUserEntityById(id);
    user.setDeletedAt(null);
    var saved = userRepository.save(user);
    eventPublisher.publishEvent(
        new AccountStatusChangedEmailEvent(
            user.getEmail(),
            true,
            user.getUpdatedAt().toLocalDateTime(),
            currentUserService.getCurrentUsername()));
    return mapper.toUserResponse(saved);
  }

  private User getUserEntityById(Long id) {
    return userRepository
        .findById(id)
        .filter(u -> u.getDeletedAt() == null)
        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
  }

  private User getDeletedUserEntityById(Long id) {
    return userRepository
        .findById(id)
        .filter(u -> u.getDeletedAt() != null)
        .orElseThrow(
            () -> new UserNotFoundException("User with id " + id + " not found or not deleted"));
  }
}
