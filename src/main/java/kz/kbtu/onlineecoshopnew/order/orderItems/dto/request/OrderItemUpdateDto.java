package kz.kbtu.onlineecoshopnew.order.orderItems.dto.request;

import jakarta.validation.constraints.Positive;

public record OrderItemUpdateDto(

        @Positive(message = "Quantity should be more than 0")
        Integer quantity
) {
}
