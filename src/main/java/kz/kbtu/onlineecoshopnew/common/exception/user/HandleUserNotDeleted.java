package kz.kbtu.onlineecoshopnew.common.exception.user;

public class HandleUserNotDeleted extends RuntimeException {
    public HandleUserNotDeleted(String email) {
        super("User with " + email + " email is not deleted");
    }
}
