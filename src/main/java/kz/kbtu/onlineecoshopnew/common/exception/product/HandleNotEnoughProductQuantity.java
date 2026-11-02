package kz.kbtu.onlineecoshopnew.common.exception.product;

import java.util.UUID;

public class HandleNotEnoughProductQuantity extends RuntimeException {
    public HandleNotEnoughProductQuantity(UUID productId, int quantity) {
        super("Quantity of product with " + productId + " id is less than " + quantity);
    }
}
