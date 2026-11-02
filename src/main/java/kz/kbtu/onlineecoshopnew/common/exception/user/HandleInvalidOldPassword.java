package kz.kbtu.onlineecoshopnew.common.exception.user;

public class HandleInvalidOldPassword extends RuntimeException {
    public HandleInvalidOldPassword() {
        super("Old password is incorrect");
    }
}
