package kz.kbtu.onlineecoshopnew.user.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        @Email
        @Size(min=3, max=40)
        String email,

        @Size(min=3, max=40, message = "Size of first name should be more than 3 symbols and less than 40 symbols")
        String firstName,

        @Size(min=3, max=40, message = "Size of last name should be more than 3 symbols and less than 40 symbols")
        String lastName,

        @Pattern(
                regexp = "^\\+7\\d{10}$",
                message = "Format of phone number should start with +7 then consists 10 digits"
        )
        String phoneNumber
) { }
