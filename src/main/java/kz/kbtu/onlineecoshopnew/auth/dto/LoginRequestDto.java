package kz.kbtu.onlineecoshopnew.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(
        @NotBlank String email,
        @NotEmpty char[] password
) {}
