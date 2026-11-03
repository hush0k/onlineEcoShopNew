package kz.kbtu.onlineecoshopnew.order.orderItems;

import kz.kbtu.onlineecoshopnew.order.orderItems.dto.request.OrderItemUpdateDto;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.response.OrderItemResponseDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderItemsMapper {

    @Named("withoutTotal")
    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "totalPrice", ignore = true)
    OrderItemResponseDto toOrderItemResponseDto(OrederItems orderItem);

    @Mapping(target = "orderId", source = "orderItem.order.id")
    @Mapping(target = "productId", source = "orderItem.product.id")
    @Mapping(target = "productName", source = "orderItem.product.name")
    @Mapping(target = "totalPrice", source = "totalPrice")
    OrderItemResponseDto toOrderItemResponseDto(OrederItems orderItem, BigDecimal totalPrice);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "price", ignore = true)
    void updateOrderItemFromDto(OrderItemUpdateDto dto, @MappingTarget OrederItems orderItem);
}
