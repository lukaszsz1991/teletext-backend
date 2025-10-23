package pl.studia.teletext.teletext_backend.api.admin.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.admin.dtos.user.CreateUserRequest;
import pl.studia.teletext.teletext_backend.api.admin.dtos.user.UserResponse;
import pl.studia.teletext.teletext_backend.domain.models.security.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "role", source = "role.name")
  UserResponse toUserResponse(User user);

  @Mapping(target = "role", ignore = true)
  @Mapping(target = "password", ignore = true)
  User toUserEntity(CreateUserRequest request);
}
