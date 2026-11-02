package kz.kbtu.onlineecoshopnew.order.orderItems.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OrderItemCreateDto(

        @NotNull(message = "Product id can not be empty!")
        UUID productId,

        @NotNull(message = "Quantity can not be empty!")
        @Positive(message = "Quantity should be more than 0")
        Integer quantity
) {
}
