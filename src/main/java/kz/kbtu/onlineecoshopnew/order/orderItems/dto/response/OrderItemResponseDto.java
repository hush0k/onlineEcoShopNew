package kz.kbtu.onlineecoshopnew.order.orderItems.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDto(
        Long id,
        Long orderId,
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal price,
        BigDecimal totalPrice
) {
}
