package kz.kbtu.onlineecoshopnew.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserCreateDto(

        @NotBlank(message = "Email can not be empty!")
        @Email
        @Size(min=3, max=40)
        String email,

        @Size(min=3, max=40, message = "Size of first name should be more than 3 symbols and less than 40 symbols")
        String firstName,

        @Size(min=3, max=40, message = "Size of last name should be more than 3 symbols and less than 40 symbols")
        String lastName,

        @Past(message = "Date of birth should be in past time")
        LocalDate dateOfBirth,

        @Pattern(
                regexp = "^\\+7\\d{10}$",
                message = "Format of phone number should start with +7 then consists 10 digits"
        )
        String phoneNumber,

        @NotEmpty(message = "Hashed password field can not be empty")
        char[] hashedPassword
) {}
