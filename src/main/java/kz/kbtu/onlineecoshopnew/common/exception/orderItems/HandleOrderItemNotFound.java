package kz.kbtu.onlineecoshopnew.common.exception.orderItems;

public class HandleOrderItemNotFound extends RuntimeException {
    public HandleOrderItemNotFound(Long orderItemId) {
        super("Order item with " + orderItemId + " id is not found");
    }
}
