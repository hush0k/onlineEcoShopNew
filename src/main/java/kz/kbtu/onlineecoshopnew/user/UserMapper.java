package kz.kbtu.onlineecoshopnew.user;

import com.onlineShop.user.dto.request.UserCreateDto;
import com.onlineShop.user.dto.request.UserUpdateDto;
import com.onlineShop.user.dto.response.UserResponseDto;
import com.onlineShop.user.dto.response.UsersListResponseDto;
import org.mapstruct.*;

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
