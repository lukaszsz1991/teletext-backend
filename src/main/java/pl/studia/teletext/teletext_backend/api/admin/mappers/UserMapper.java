package pl.studia.teletext.teletext_backend.api.admin.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.studia.teletext.teletext_backend.api.admin.dtos.UserResponse;
import pl.studia.teletext.teletext_backend.domain.models.security.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "role", source = "role.name")
  UserResponse toUserResponse(User user);
}
