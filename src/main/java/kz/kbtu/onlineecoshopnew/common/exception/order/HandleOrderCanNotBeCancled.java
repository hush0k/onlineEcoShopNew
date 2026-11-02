package kz.kbtu.onlineecoshopnew.common.exception.order;

import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderStatus;

public class HandleOrderCanNotBeCancled extends RuntimeException {
    public HandleOrderCanNotBeCancled(OrderStatus current) {
        super("Order status can not be cancled in " + current.name() + " stage");
    }
}
