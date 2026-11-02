package kz.kbtu.onlineecoshopnew.common.exception.category;

public class HandleCategoryAlreadyExists extends RuntimeException {
    public HandleCategoryAlreadyExists(String name) {
        super("Category with name " + name + " already exists");
    }
}
