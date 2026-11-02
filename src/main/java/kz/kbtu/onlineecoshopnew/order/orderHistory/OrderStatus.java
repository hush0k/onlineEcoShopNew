package kz.kbtu.onlineecoshopnew.order.orderHistory;

import kz.kbtu.onlineecoshopnew.common.exception.order.HandleOrderAlreadyCompleted;

import java.util.List;

public enum OrderStatus {
    CREATED(1, List.of(2, 9)),
    PENDING_PAYMENT(2, List.of(3, 9)),
    PAID(3, List.of(4, 9)),
    PROCESSING(4, List.of(5, 9)),
    SHIPPED(5, List.of(6, 9)),
    IN_TRANSIT(6, List.of(7, 9)),
    READY_FOR_PICKUP(7, List.of(8, 9)),
    DELIVERED(8, List.of(10)),
    CANCELLED(9, List.of(10)),
    RETURNED(10, List.of());

    OrderStatus(Integer sequenceNumber, List<Integer> possibleNextStep) {
        this.sequenceNumber = sequenceNumber;
        this.possibleNextStep = possibleNextStep;
    }

    public OrderStatus getNextStep(OrderStatus status) {
        Integer nextStep = possibleNextStep.get(sequenceNumber + 1);
        if (nextStep == null || nextStep >= 9) {
            throw new HandleOrderAlreadyCompleted(this);
        }
        return Enum.valueOf(OrderStatus.class, nextStep.toString());
    }

    public boolean canTransitionTo(OrderStatus next) {
        return possibleNextStep.contains(next.sequenceNumber);
    }

    private Integer sequenceNumber;
    private List<Integer> possibleNextStep;
}
