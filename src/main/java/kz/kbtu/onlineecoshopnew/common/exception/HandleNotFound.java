package kz.kbtu.onlineecoshopnew.common.exception;

public class HandleNotFound extends RuntimeException {

    public HandleNotFound(String entity, Object id) {
        this(entity, "id", id);
    }

    public HandleNotFound(String entity, String field, Object value) {
        super(entity + " with " + field + " " + value + " is not found");
    }
}
