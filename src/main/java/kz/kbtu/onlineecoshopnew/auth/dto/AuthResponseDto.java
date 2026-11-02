package kz.kbtu.onlineecoshopnew.auth.dto;

public record AuthResponseDto(
        String accessToken,
        String refreshToken
) {}
