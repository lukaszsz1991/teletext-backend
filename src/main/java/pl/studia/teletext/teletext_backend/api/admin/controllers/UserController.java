package pl.studia.teletext.teletext_backend.api.admin.controllers;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.studia.teletext.teletext_backend.api.admin.dtos.user.ChangeUserPasswordRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.user.CreateUserRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.user.UpdateUserRequest;
import pl.studia.teletext.teletext_backend.domain.services.UserService;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<?> getAllUsers(
    @RequestParam(required = false) Boolean includeDeleted
  ) {
    includeDeleted = includeDeleted != null && includeDeleted;
    var users = userService.getAllUsers(includeDeleted);
    return ResponseEntity.ok(users);
  }

  @GetMapping("{id}")
  public ResponseEntity<?> getUser(
    @PathVariable Long id
  ) {
    var user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<?> createUser(
    @Valid @RequestBody CreateUserRequest request
    ) {
    var user = userService.createUser(request);
    return ResponseEntity
      .created(URI.create("/api/admin/users/" + user.id()))
      .body(user);
  }

  @PutMapping("{id}")
  public ResponseEntity<?> updateUser(
    @PathVariable Long id,
    @Valid @RequestBody UpdateUserRequest request
  ) {
    var user = userService.updateUser(id, request);
    return ResponseEntity.ok(user);
  }

  @PutMapping("{id}/change-password")
  public ResponseEntity<?> changeUserPassword(
    @PathVariable Long id,
    @Valid @RequestBody ChangeUserPasswordRequest request
  ) {
    userService.changePassword(id, request);
    return ResponseEntity.noContent().build();
  }

  //TODO: in documentation mention that this is a soft delete
  @DeleteMapping("{id}")
  public ResponseEntity<?> deleteUser(
    @PathVariable Long id
  ) {
    userService.deleteUserById(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}/restore")
  public ResponseEntity<?> restoreUser(
    @PathVariable Long id
  ) {
    userService.restoreUserById(id);
    return ResponseEntity.noContent().build();
  }
}
