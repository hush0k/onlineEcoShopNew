package kz.kbtu.onlineecoshopnew.order.orderHistory;

import kz.kbtu.onlineecoshopnew.order.orderHistory.dto.request.OrderHistoryCreateDto;
import kz.kbtu.onlineecoshopnew.order.orderHistory.dto.response.OrderHistoryListDto;
import kz.kbtu.onlineecoshopnew.order.orderHistory.dto.response.OrderHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderHistoryMapper orderHistoryMapper;

    public OrderHistoryResponseDto createOrderHistory(OrderHistoryCreateDto dto) {
        OrderHistory orderHistory = orderHistoryMapper.toOrderHistory(dto);
        orderHistoryRepository.save(orderHistory);
        return orderHistoryMapper.toOrderHistoryResponseDto(orderHistory);
    }

    @Transactional(readOnly = true)
    public List<OrderHistory> getOrderHistoryByOrderId(Long orderId) {
        List<OrderHistory> orderHistories = orderHistoryRepository.findByOrderId(orderId);
        if (orderHistories.isEmpty()) {
            return List.of();
        }
        orderHistories.sort((Comparator.comparing(OrderHistory::getStatus)));
        return orderHistories;
    }
}
