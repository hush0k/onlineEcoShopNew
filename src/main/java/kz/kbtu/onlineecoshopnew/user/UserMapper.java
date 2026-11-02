package kz.kbtu.onlineecoshopnew.user;


import kz.kbtu.onlineecoshopnew.user.dto.request.UserCreateDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserUpdateDto;
import kz.kbtu.onlineecoshopnew.user.dto.response.UserResponseDto;
import kz.kbtu.onlineecoshopnew.user.dto.response.UsersListResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserCreateDto createDto);
    UserResponseDto toUserResponse(User user);

    @Mapping(target = "users", source = "users")
    @Mapping(target = "total", source = "total")
    UsersListResponseDto toUsersListResponseDto(List<User> users, int total);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);

}
