package kz.kbtu.onlineecoshopnew.order.order;

import kz.kbtu.onlineecoshopnew.order.order.dto.request.OrderCreateDto;
import kz.kbtu.onlineecoshopnew.order.order.dto.request.OrderUpdateDto;
import kz.kbtu.onlineecoshopnew.order.order.dto.response.OrderResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrder(OrderCreateDto dto);

    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toOrderResponseDto(Order order);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrderFromDto(OrderUpdateDto dto, @MappingTarget Order order);
}
