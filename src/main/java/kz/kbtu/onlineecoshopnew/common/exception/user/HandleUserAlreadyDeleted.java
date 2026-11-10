package kz.kbtu.onlineecoshopnew.common.exception.user;

public class HandleUserAlreadyDeleted extends RuntimeException {
    public HandleUserAlreadyDeleted(Long userId) {
        super("User with " + userId + " id is already deleted");
    }
}
