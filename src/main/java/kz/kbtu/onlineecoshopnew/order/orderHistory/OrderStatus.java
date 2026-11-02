package kz.kbtu.onlineecoshopnew.order.orderHistory;

import kz.kbtu.onlineecoshopnew.common.exception.order.HandleOrderAlreadyCompleted;
import kz.kbtu.onlineecoshopnew.common.exception.order.HandleOrderCanNotBeCancled;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    CREATED, PENDING_PAYMENT, PAID, PROCESSING, SHIPPED,
    IN_TRANSIT, READY_FOR_PICKUP, DELIVERED, CANCELLED, RETURNED;

    private Set<OrderStatus> allowedNext;

    static {
        CREATED.allowedNext          = EnumSet.of(PENDING_PAYMENT, CANCELLED);
        PENDING_PAYMENT.allowedNext  = EnumSet.of(PAID, CANCELLED);
        PAID.allowedNext             = EnumSet.of(PROCESSING, CANCELLED);
        PROCESSING.allowedNext       = EnumSet.of(SHIPPED, CANCELLED);
        SHIPPED.allowedNext          = EnumSet.of(IN_TRANSIT, CANCELLED);
        IN_TRANSIT.allowedNext       = EnumSet.of(READY_FOR_PICKUP, CANCELLED);
        READY_FOR_PICKUP.allowedNext = EnumSet.of(DELIVERED, CANCELLED);
        DELIVERED.allowedNext        = EnumSet.of(RETURNED);
        CANCELLED.allowedNext        = EnumSet.noneOf(OrderStatus.class);
        RETURNED.allowedNext         = EnumSet.noneOf(OrderStatus.class);
    }

    public boolean canTransitionTo(OrderStatus next) {
        return allowedNext.contains(next);
    }

    public boolean isFinal() {
        return allowedNext.isEmpty();
    }

    public OrderStatus next() {
        return allowedNext.stream()
                .filter(s -> s != CANCELLED && s != RETURNED)
                .findFirst()
                .orElseThrow(() -> new HandleOrderAlreadyCompleted(this));
    }

    public OrderStatus cancel() {
        if (!canTransitionTo(CANCELLED)) {
            throw new HandleOrderCanNotBeCancled(this);
        }
        return CANCELLED;
    }

}