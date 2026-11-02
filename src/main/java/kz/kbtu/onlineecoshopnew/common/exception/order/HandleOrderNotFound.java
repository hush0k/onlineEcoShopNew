package kz.kbtu.onlineecoshopnew.common.exception.order;

public class HandleOrderNotFound extends RuntimeException {
    public HandleOrderNotFound(Long orderId) {
        super("Order with " + orderId + " id is not found");
    }
}
