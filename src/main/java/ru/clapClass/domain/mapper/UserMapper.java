package ru.clapClass.domain.mapper;

import org.mapstruct.*;
import ru.clapClass.domain.dto.auth.UserRequest;
import ru.clapClass.domain.dto.auth.UserResponse;
import ru.clapClass.domain.models.user.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(source = "avatar.path", target = "avatar")
    UserResponse toUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserRequest dto, @MappingTarget User user);
}

