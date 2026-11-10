package kz.kbtu.onlineecoshopnew.common.exception;

public class HandleAlreadyExists extends RuntimeException {

    public HandleAlreadyExists(String entity, String field, Object value) {
        super(entity + " with " + field + " " + value + " already exists");
    }
}
