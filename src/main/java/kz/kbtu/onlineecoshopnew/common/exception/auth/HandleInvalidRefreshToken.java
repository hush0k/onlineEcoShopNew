package kz.kbtu.onlineecoshopnew.common.exception.auth;

public class HandleInvalidRefreshToken extends RuntimeException {
    public HandleInvalidRefreshToken() {
        super("Refresh token is invalid or expired");
    }
}
