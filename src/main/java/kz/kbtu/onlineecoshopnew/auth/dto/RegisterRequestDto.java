package kz.kbtu.onlineecoshopnew.auth.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.nio.CharBuffer;
import java.time.LocalDate;
import java.util.Arrays;

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

        @NotEmpty(message = "Password field can not be empty")
        char[] password,

        @NotEmpty(message = "Password field can not be empty")
        char[] confirmPassword
) {
    private static final java.util.regex.Pattern PASSWORD_PATTERN =
            java.util.regex.Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,72}$");

    @AssertTrue(message = "Your password must contain English letters and digits. Size should not be less than 8 " +
            "characters")
    public boolean isPasswordValid() {
        if (password == null) {
            return true;
        }
        return PASSWORD_PATTERN.matcher(CharBuffer.wrap(password)).matches();
    }

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordConfirmed() {
        if (password == null || confirmPassword == null) {
            return true;
        }
        return Arrays.equals(password, confirmPassword);
    }
}

