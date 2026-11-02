package kz.kbtu.onlineecoshopnew.user.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserUpdateDto(
        @Email
        @Size(min=3, max=100)
        String email,

        @Size(min=3, max=100, message = "Size of first name should be more than 3 symbols and less than 100 symbols")
        String firstName,

        @Size(min=3, max=100, message = "Size of last name should be more than 3 symbols and less than 100 symbols")
        String lastName,

        @Pattern(
                regexp = "^\\+7\\d{10}$",
                message = "Format of phone number should start with +7 then consists 10 digits"
        )
        String phoneNumber
) { }
