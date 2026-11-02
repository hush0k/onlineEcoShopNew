package kz.kbtu.onlineecoshopnew.user.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserPasswordChangeDto(

        @NotBlank(message = "This field cannot be empty")
        String oldPassword,

        @NotBlank(message = "Password field can not be empty")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,72}$",
                message = "Your password must contain English letters and digits. Size should not be less than 8 " +
                        "characters"
        )
        String newPassword,

        @NotBlank(message = "Password field can not be empty")
        String confirmPassword

) {
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordConfirmed() {
        if (newPassword == null || confirmPassword == null) {
            return true;
        }
        return newPassword.equals(confirmPassword);
    }
}
