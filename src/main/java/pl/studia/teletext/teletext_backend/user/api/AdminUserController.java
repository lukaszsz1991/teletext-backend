package pl.studia.teletext.teletext_backend.user.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
import pl.studia.teletext.teletext_backend.user.api.dto.ChangeUserPasswordRequest;
import pl.studia.teletext.teletext_backend.user.api.dto.CreateUserRequest;
import pl.studia.teletext.teletext_backend.user.api.dto.UpdateUserRequest;
import pl.studia.teletext.teletext_backend.user.api.dto.UserResponse;
import pl.studia.teletext.teletext_backend.user.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Users management controller", description = "CRUD operations for users.")
public class AdminUserController {

  private final UserService userService;

  @Operation(
      summary = "Get all users",
      description = "Returns a list of users. Optionally include deleted users.")
  @ApiResponse(responseCode = "200", description = "Users found, or empty list if none exist.")
  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers(
      @RequestParam(defaultValue = "false") Boolean includeDeleted) {
    var users = userService.getAllUsers(includeDeleted);
    return ResponseEntity.ok(users);
  }

  @Operation(summary = "Get user by ID", description = "Returns a single user by its ID.")
  @ApiResponse(responseCode = "200", description = "User found successfully.")
  @GetMapping("{id}")
  public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
    var user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @Operation(
      summary = "Create user",
      description =
          "Creates a new user and returns the created user and proper location in the header.")
  @ApiResponse(responseCode = "201", description = "User successfully created.")
  @PostMapping
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
    var user = userService.createUser(request);
    return ResponseEntity.created(URI.create("/api/admin/users/" + user.id())).body(user);
  }

  @Operation(
      summary = "Update user",
      description = "Updates an existing user and returns the updated entity.")
  @ApiResponse(responseCode = "200", description = "User successfully updated.")
  @PutMapping("{id}")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
    var user = userService.updateUser(id, request);
    return ResponseEntity.ok(user);
  }

  @Operation(
      summary = "Change user password",
      description = "Changes the password of a user. No content returned.")
  @ApiResponse(
      responseCode = "204",
      description = "Password changed successfully.",
      content = @Content)
  @PutMapping("{id}/change-password")
  public ResponseEntity<?> changeUserPassword(
      @PathVariable Long id, @Valid @RequestBody ChangeUserPasswordRequest request) {
    userService.changePassword(id, request);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Soft delete user",
      description = "Marks the user as deleted. No content returned.")
  @ApiResponse(
      responseCode = "204",
      description = "User soft-deleted successfully.",
      content = @Content)
  @DeleteMapping("{id}")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Restore user",
      description = "Restores a previously soft deleted user. No content returned.")
  @ApiResponse(
      responseCode = "204",
      description = "User restored successfully.",
      content = @Content)
  @PutMapping("{id}/restore")
  public ResponseEntity<?> restoreUser(@PathVariable Long id) {
    userService.restoreUserById(id);
    return ResponseEntity.noContent().build();
  }
}
