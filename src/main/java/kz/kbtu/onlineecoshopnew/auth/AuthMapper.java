package kz.kbtu.onlineecoshopnew.auth;

import kz.kbtu.onlineecoshopnew.auth.dto.RegisterRequestDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "email", source = "email")
    @Mapping(target = "hashedPassword", source = "hashedPassword")
    UserCreateDto toUserCreateDto(RegisterRequestDto dto, String email, String hashedPassword);
}
