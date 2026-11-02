package kz.kbtu.onlineecoshopnew.common.exception.order;

import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderStatus;

public class HandleOrderAlreadyCompleted extends RuntimeException {
    public HandleOrderAlreadyCompleted(OrderStatus status) {
        super("Next step is not possible: order has already been " + status.name().toLowerCase());
    }

    public HandleOrderAlreadyCompleted(OrderStatus current, OrderStatus next) {
        super("Status transition from " + current.name() + " to " + next.name() + " is not allowed");
    }
}
