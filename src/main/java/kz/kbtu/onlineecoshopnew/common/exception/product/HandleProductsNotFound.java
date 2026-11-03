package kz.kbtu.onlineecoshopnew.common.exception.product;

import java.util.Set;
import java.util.UUID;

public class HandleProductsNotFound extends RuntimeException {
    public HandleProductsNotFound(Set<UUID> productIds) {
        super("Product with " + productIds + " id is not found");
    }
}
