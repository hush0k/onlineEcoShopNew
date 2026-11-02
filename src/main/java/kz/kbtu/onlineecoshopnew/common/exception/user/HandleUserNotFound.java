package kz.kbtu.onlineecoshopnew.common.exception.user;

public class HandleUserNotFound extends RuntimeException {
    public HandleUserNotFound(Long user_id) {
        super("User with " + user_id + " id is not found");
    }
}
