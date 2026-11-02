package kz.kbtu.onlineecoshopnew.user.dto.response;

import java.util.List;

public record UsersListResponseDto(
        List<UserResponseDto> users,
        int total
) {}
