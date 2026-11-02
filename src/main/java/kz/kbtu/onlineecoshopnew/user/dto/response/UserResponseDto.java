package kz.kbtu.onlineecoshopnew.user.dto.response;


import kz.kbtu.onlineecoshopnew.user.UserRole;

import java.time.Instant;
import java.time.LocalDate;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        UserRole userRole,
        String phoneNumber,
        Instant createdAt,
        Instant updatedAt
) {}
