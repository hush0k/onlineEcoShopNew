package kz.kbtu.onlineecoshopnew.order.orderHistory;

import kz.kbtu.onlineecoshopnew.order.orderHistory.dto.request.OrderHistoryCreateDto;
import kz.kbtu.onlineecoshopnew.order.orderHistory.dto.response.OrderHistoryResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderHistoryMapper {

    OrderHistory toOrderHistory(OrderHistoryCreateDto dto);
    OrderHistoryResponseDto toOrderHistoryResponseDto(OrderHistory orderHistory);

}
