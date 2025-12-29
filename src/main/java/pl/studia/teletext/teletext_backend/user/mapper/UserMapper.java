package pl.studia.teletext.teletext_backend.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.auth.domain.User;
import pl.studia.teletext.teletext_backend.user.api.dto.CreateUserRequest;
import pl.studia.teletext.teletext_backend.user.api.dto.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "role", source = "role.name")
  UserResponse toUserResponse(User user);

  @Mapping(target = "role", ignore = true)
  @Mapping(target = "password", ignore = true)
  User toUserEntity(CreateUserRequest request);
}
