package kz.kbtu.onlineecoshopnew.common.exception.order;

import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderStatus;

public class HandleOrderAlreadyCompleted extends RuntimeException {
    public HandleOrderAlreadyCompleted(OrderStatus current) {
        super("Status transition from " + current.name() + " is not allowed");
    }
}
