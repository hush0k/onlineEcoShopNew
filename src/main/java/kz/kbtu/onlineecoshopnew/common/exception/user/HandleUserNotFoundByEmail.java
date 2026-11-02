package kz.kbtu.onlineecoshopnew.common.exception.user;

public class HandleUserNotFoundByEmail extends RuntimeException {
    public HandleUserNotFoundByEmail(String email) {
        super("User with " + email + " email is not found");
    }
}
