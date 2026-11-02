package kz.kbtu.onlineecoshopnew.order.order.dto.request;

import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateDto(

        @NotNull(message = "Status can not be empty!")
        OrderStatus status
) {
}
