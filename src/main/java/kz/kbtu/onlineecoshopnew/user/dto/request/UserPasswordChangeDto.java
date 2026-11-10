package kz.kbtu.onlineecoshopnew.user.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.regex.Pattern;

public record UserPasswordChangeDto(

        @NotEmpty(message = "This field cannot be empty")
        char[] oldPassword,

        @NotEmpty(message = "Password field can not be empty")
        char[] newPassword,

        @NotEmpty(message = "Password field can not be empty")
        char[] confirmPassword

) {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,72}$");

    @AssertTrue(message = "Your password must contain English letters and digits. Size should not be less than 8 " +
            "characters")
    public boolean isNewPasswordValid() {
        if (newPassword == null) {
            return true;
        }
        return PASSWORD_PATTERN.matcher(CharBuffer.wrap(newPassword)).matches();
    }

    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordConfirmed() {
        if (newPassword == null || confirmPassword == null) {
            return true;
        }
        return Arrays.equals(newPassword, confirmPassword);
    }
}
