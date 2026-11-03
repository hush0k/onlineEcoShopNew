package kz.kbtu.onlineecoshopnew.order.orderHistory.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderStatus;

public record OrderHistoryCreateDto(

        @NotNull
        @Positive
        Long order_id,

        @NotNull
        OrderStatus orderStatus

) {
}
