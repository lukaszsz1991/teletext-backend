package pl.studia.teletext.teletext_backend.domain.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.admin.dtos.user.ChangeUserPasswordRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.user.CreateUserRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.user.UpdateUserRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.user.UserResponse;
import pl.studia.teletext.teletext_backend.api.admin.mappers.UserMapper;
import pl.studia.teletext.teletext_backend.domain.models.security.User;
import pl.studia.teletext.teletext_backend.domain.repositories.UserRepository;
import pl.studia.teletext.teletext_backend.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;

  public List<UserResponse> getAllUsers(boolean includeDeleted) {
    var users = userRepository.findAll();
    return users.stream()
      .filter(u -> u.getDeletedAt() == null || includeDeleted)
      .map(mapper::toUserResponse)
      .toList();
  }

  public UserResponse getUserById(Long id) {
    var user = getUserEntityById(id);
    return mapper.toUserResponse(user);
  }

  public UserResponse createUser(CreateUserRequest request) {
    var user = mapper.toUserEntity(request);
    user.setPassword(passwordEncoder.encode(request.password()));
    user = userRepository.save(user);
    return mapper.toUserResponse(user);
  }

  public UserResponse updateUser(Long id, UpdateUserRequest request) {
    var user = getUserEntityById(id);
    user.setUsername(request.username());
    user.setEmail(request.email());
    user = userRepository.save(user);
    return mapper.toUserResponse(user);
  }

  public void changePassword(Long id, ChangeUserPasswordRequest request) {
    var user = getUserEntityById(id);
    user.setPassword(passwordEncoder.encode(request.password()));
    userRepository.save(user);
  }

  public void deleteUserById(Long id) {
    // TODO: prevent deleting own account
    var user = getUserEntityById(id);
    user.setDeletedAt(Timestamp.from(Instant.now()));
    userRepository.save(user);
  }

  public void restoreUserById(Long id) {
    var user = getDeletedUserEntityById(id);
    user.setDeletedAt(null);
    userRepository.save(user);
  }

  private User getUserEntityById(Long id) {
    return userRepository.findById(id)
      .filter(u -> u.getDeletedAt() == null)
      .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
  }

  private User getDeletedUserEntityById(Long id) {
    return userRepository.findById(id)
      .filter(u -> u.getDeletedAt() != null)
      .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found or not deleted"));
  }
}
