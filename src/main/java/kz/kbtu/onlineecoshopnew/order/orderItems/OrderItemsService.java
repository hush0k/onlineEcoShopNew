package kz.kbtu.onlineecoshopnew.order.orderItems;

import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import kz.kbtu.onlineecoshopnew.common.exception.HandleNotFound;
import kz.kbtu.onlineecoshopnew.order.order.Order;
import kz.kbtu.onlineecoshopnew.order.order.OrderRepository;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.request.OrderItemCreateDto;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.request.OrderItemUpdateDto;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.response.OrderItemResponseDto;
import kz.kbtu.onlineecoshopnew.product.Product.Product;
import kz.kbtu.onlineecoshopnew.product.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemsService {

    private final OrderItemsRepository orderItemsRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemsMapper orderItemsMapper;

    private OrederItems orderItemIsExists(Long orderId, Long orderItemId) {
        return orderItemsRepository.findByIdAndOrderId(orderItemId, orderId).orElseThrow(
                () -> new HandleNotFound("Order item", orderItemId)
        );
    }

    private Order orderIsExists(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new HandleNotFound("Order", orderId)
        );
    }

    private Product productIsExists(UUID productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new HandleNotFound("Product", productId)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<OrderItemResponseDto>> getOrderItem(Long orderId, Long orderItemId) {
        return ApiResponse.ok("Order item successfully retrieved",
                orderItemsMapper.toOrderItemResponseDto(orderItemIsExists(orderId, orderItemId)));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Page<OrderItemResponseDto>>> getOrderItems(Long orderId, Pageable pageable) {
        orderIsExists(orderId);
        return ApiResponse.ok("Order items successfully retrieved",
                orderItemsRepository.findAllByOrderId(orderId, pageable).map(orderItemsMapper::toOrderItemResponseDto));
    }

    public ResponseEntity<ApiResponse<OrderItemResponseDto>> createOrderItem(Long orderId, OrderItemCreateDto dto) {
        Order order = orderIsExists(orderId);
        Product product = productIsExists(dto.productId());
        OrederItems orderItem = OrederItems.builder()
                .order(order)
                .product(product)
                .quantity(dto.quantity())
                .build();
        orderItemsRepository.save(orderItem);
        BigDecimal totalPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(dto.quantity()));
        return ApiResponse.created("Order item successfully created",
                orderItemsMapper.toOrderItemResponseDto(orderItem, totalPrice));
    }

    public ResponseEntity<ApiResponse<OrderItemResponseDto>> updateOrderItem(Long orderId, Long orderItemId, OrderItemUpdateDto dto) {
        OrederItems orderItem = orderItemIsExists(orderId, orderItemId);
        orderItemsMapper.updateOrderItemFromDto(dto, orderItem);
        orderItemsRepository.flush();
        return ApiResponse.ok("Order item successfully updated", orderItemsMapper.toOrderItemResponseDto(orderItem));
    }

    public ResponseEntity<ApiResponse<Void>> deleteOrderItem(Long orderId, Long orderItemId) {
        OrederItems orderItem = orderItemIsExists(orderId, orderItemId);
        orderItemsRepository.delete(orderItem);
        return ApiResponse.ok("Order item successfully deleted");
    }

}
