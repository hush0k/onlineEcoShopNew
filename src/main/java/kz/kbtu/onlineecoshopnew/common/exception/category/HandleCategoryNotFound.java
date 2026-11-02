package kz.kbtu.onlineecoshopnew.common.exception.category;

import java.util.UUID;

public class HandleCategoryNotFound extends RuntimeException {
    public HandleCategoryNotFound(UUID categoryId) {
        super("Category with " + categoryId + " id is not found");
    }
}
