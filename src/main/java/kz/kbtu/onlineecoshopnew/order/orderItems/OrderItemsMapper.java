package kz.kbtu.onlineecoshopnew.order.orderItems;

import kz.kbtu.onlineecoshopnew.order.orderItems.dto.request.OrderItemUpdateDto;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.response.OrderItemResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OrderItemsMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemResponseDto toOrderItemResponseDto(OrederItems orderItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "price", ignore = true)
    void updateOrderItemFromDto(OrderItemUpdateDto dto, @MappingTarget OrederItems orderItem);
}
