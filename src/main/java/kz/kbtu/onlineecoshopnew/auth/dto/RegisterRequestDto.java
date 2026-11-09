package kz.kbtu.onlineecoshopnew.auth.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequestDto(

        @NotBlank(message = "Email can not be empty!")
        @Email
        @Size(min=3, max=50)
        String email,

        @Size(min=3, max=50, message = "Size of first name should be more than 3 symbols and less than 100 symbols")
        String firstName,

        @Size(min=3, max=50, message = "Size of last name should be more than 3 symbols and less than 100 symbols")
        String lastName,

        @Past(message = "Date of birth should be in past time")
        LocalDate dateOfBirth,

        @Pattern(
                regexp = "^\\+7\\d{10}$",
                message = "Format of phone number should start with +7 then consists 10 digits"
        )
        String phoneNumber,

        @NotBlank(message = "Password field can not be empty")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,72}$",
                message = "Your password must contain English letters and digits. Size should not be less than 8 " +
                        "characters"
        )
        String password,

        @NotBlank(message = "Password field can not be empty")
        String confirmPassword
) {
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordConfirmed() {
        if (password == null || confirmPassword == null) {
            return true;
        }
        return password.equals(confirmPassword);
    }
}

