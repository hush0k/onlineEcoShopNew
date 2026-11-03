package kz.kbtu.onlineecoshopnew.order.orderHistory.dto.response;

import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderHistory;

import java.util.List;

public record OrderHistoryListDto(
        List<OrderHistory> orderHistoryList
) {
}
