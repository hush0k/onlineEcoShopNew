package kz.kbtu.onlineecoshopnew.common.exception.auth;

public class HandleUserAlreadyExists extends RuntimeException {
    public HandleUserAlreadyExists(String email) {
        super("User with email: " + email + " is already exists.");
    }
}
