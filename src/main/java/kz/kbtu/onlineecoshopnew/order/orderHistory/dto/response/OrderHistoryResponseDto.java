package kz.kbtu.onlineecoshopnew.order.orderHistory.dto.response;

import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderHistory;

import java.time.Instant;

public record OrderHistoryResponseDto(

        Long id,
        Long order_id,
        OrderHistory orderHistory,
        Instant timestamp

) {
}
