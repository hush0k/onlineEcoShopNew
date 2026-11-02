package kz.kbtu.onlineecoshopnew.common.exception.product;

import java.util.UUID;

public class HandleProductNotFound extends RuntimeException {
    public HandleProductNotFound(UUID productId) {
        super("Product with " + productId + " id is not found");
    }
}
