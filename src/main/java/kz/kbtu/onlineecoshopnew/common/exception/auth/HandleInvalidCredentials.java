package kz.kbtu.onlineecoshopnew.common.exception.auth;

public class HandleInvalidCredentials extends RuntimeException {
    public HandleInvalidCredentials() {
        super("Invalid email or password");
    }
}
