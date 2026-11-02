package kz.kbtu.onlineecoshopnew.order.order.dto.response;

import kz.kbtu.onlineecoshopnew.order.order.Currency;
import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponseDto(
        Long id,
        Long userId,
        OrderStatus status,
        BigDecimal subPrice,
        BigDecimal deliveryPrice,
        BigDecimal discount,
        BigDecimal totalPrice,
        Currency currency,
        String deliveryCity,
        String deliveryAddress,
        Instant predictedDateOfDelivery,
        Instant createdAt
) {
}
