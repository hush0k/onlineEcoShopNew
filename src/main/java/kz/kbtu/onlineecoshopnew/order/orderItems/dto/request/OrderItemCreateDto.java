package kz.kbtu.onlineecoshopnew.order.orderItems.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderItemCreateDto(

        @NotNull(message = "Product id can not be empty!")
        UUID productId,

        @NotNull(message = "Quantity can not be empty!")
        @Positive(message = "Quantity should be more than 0")
        Integer quantity
) {
}
