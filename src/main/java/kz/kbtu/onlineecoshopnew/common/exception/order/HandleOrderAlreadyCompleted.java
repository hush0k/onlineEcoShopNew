package kz.kbtu.onlineecoshopnew.common.exception.order;

import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderStatus;

public class HandleOrderAlreadyCompleted extends RuntimeException {
    public HandleOrderAlreadyCompleted(OrderStatus current, OrderStatus next) {
        super("Status transition from " + current.name() + " to " + next.name() + " is not allowed");
    }
}
