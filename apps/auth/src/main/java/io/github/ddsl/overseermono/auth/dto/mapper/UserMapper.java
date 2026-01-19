package io.github.ddsl.overseermono.auth.dto.mapper;

import io.github.ddsl.overseermono.auth.dto.CreateUserDto;
import io.github.ddsl.overseermono.commonlib.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "blocked", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    User toUser (CreateUserDto createUserRequestDto);
}
