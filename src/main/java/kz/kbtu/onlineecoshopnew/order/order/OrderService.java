package kz.kbtu.onlineecoshopnew.order.order;

import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import kz.kbtu.onlineecoshopnew.common.exception.HandleNotFound;

import kz.kbtu.onlineecoshopnew.common.exception.order.HandleOrderAlreadyCompleted;
import kz.kbtu.onlineecoshopnew.order.order.dto.request.OrderCreateDto;
import kz.kbtu.onlineecoshopnew.order.order.dto.request.OrderStatusUpdateDto;
import kz.kbtu.onlineecoshopnew.order.order.dto.request.OrderUpdateDto;
import kz.kbtu.onlineecoshopnew.order.order.dto.response.OrderResponseDto;
import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderHistory;
import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderHistoryRepository;
import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderHistoryService;
import kz.kbtu.onlineecoshopnew.order.orderHistory.OrderStatus;
import kz.kbtu.onlineecoshopnew.order.orderHistory.dto.request.OrderHistoryCreateDto;
import kz.kbtu.onlineecoshopnew.order.orderItems.OrderItemsService;
import kz.kbtu.onlineecoshopnew.order.orderItems.OrederItems;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.request.OrderItemCreateDto;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.response.OrderItemResponseDto;
import kz.kbtu.onlineecoshopnew.product.Product.Product;
import kz.kbtu.onlineecoshopnew.product.Product.ProductService;
import kz.kbtu.onlineecoshopnew.user.User;
import kz.kbtu.onlineecoshopnew.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderItemsService orderItemsService;
    private final ProductService productService;
    private final OrderMapper orderMapper;
    private final UserRepository userService;
    private final OrderHistoryService orderHistoryService;
    private static final BigDecimal DELIVERY_RATE = new BigDecimal("0.15");
    private static final ZoneId ZONE = ZoneId.of("Asia/Almaty");

    private Order orderIsExists(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new HandleNotFound("Order", orderId)
        );
    }

    private void changeStatus(Order order, OrderStatus newStatus) {
        order.setStatus(newStatus);
        orderHistoryRepository.save(
                OrderHistory.builder()
                        .order(order)
                        .status(newStatus)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrder(Long orderId) {
        return ApiResponse.ok("Order successfully retrieved", orderMapper.toOrderResponseDto(orderIsExists(orderId)));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getAllOrders(Pageable pageable) {
        return ApiResponse.ok("Orders successfully retrieved", orderRepository.findAll(pageable).map(orderMapper::toOrderResponseDto));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getMyOrders(String email, Pageable pageable) {
        return ApiResponse.ok("Orders successfully retrieved", orderRepository.findAllByUserEmail(email, pageable).map(orderMapper::toOrderResponseDto));
    }

    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(Long orderId, OrderUpdateDto dto) {
        Order order = orderIsExists(orderId);
        orderMapper.updateOrderFromDto(dto, order);
        orderRepository.flush();
        return ApiResponse.ok("Order successfully updated", orderMapper.toOrderResponseDto(order));
    }

    public ResponseEntity<ApiResponse<OrderResponseDto>> updateStatusOfOrder(Long orderId, OrderStatusUpdateDto dto) {
        Order order = orderIsExists(orderId);
        OrderStatus current = order.getStatus();

        if (!current.canTransitionTo(dto.status())) {
            throw new HandleOrderAlreadyCompleted(current);
        }

        changeStatus(order, dto.status());
        return ApiResponse.ok("Order status successfully updated", orderMapper.toOrderResponseDto(order));
    }

    public ResponseEntity<ApiResponse<Void>> deleteOrder(Long orderId) {
        Order order = orderIsExists(orderId);
        orderHistoryRepository.deleteAllByOrder(order);
        orderRepository.delete(order);
        return ApiResponse.ok("Order successfully deleted");
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long orderId, String email) {
        return orderRepository.existsByIdAndUserEmail(orderId, email);
    }

    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrderStatus(Long orderId) {
        Order order = orderIsExists(orderId);
        changeStatus(order, order.getStatus().next());
        return ApiResponse.ok("Order moved to next status", orderMapper.toOrderResponseDto(order));
    }

    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(Long orderId) {
        Order order = orderIsExists(orderId);
        changeStatus(order, order.getStatus().cancel());
        return ApiResponse.ok("Order successfully cancelled", orderMapper.toOrderResponseDto(order));
    }

    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(OrderCreateDto dto, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new HandleNotFound("User", "email", email));

        Set<UUID> ids = dto.items().stream()
                .map(OrderItemCreateDto::productId)
                .collect(Collectors.toSet());
        Map<UUID, Product> products = productService.getProductsById(ids).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));
        if (products.size() != ids.size()) {
            Set<UUID> missing = new HashSet<>(ids);
            missing.removeAll(products.keySet());
            throw new HandleNotFound("Product", "ids", missing);
        }

        Order order = orderMapper.toOrder(dto);
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setPredictedDateOfDelivery(LocalDate.now(ZONE).plusDays(10));

        BigDecimal subPrice = BigDecimal.ZERO;
        for (OrderItemCreateDto itemDto : dto.items()) {
            Product product = products.get(itemDto.productId());
            OrederItems item = new OrederItems();
            item.setProduct(product);
            item.setQuantity(itemDto.quantity());
            item.setPrice(product.getPrice());
            order.addItem(item);
            subPrice = subPrice.add(item.getPrice().multiply(new BigDecimal(itemDto.quantity())));
        }

        BigDecimal deliveryPrice = subPrice.multiply(DELIVERY_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        order.setSubPrice(subPrice);
        order.setDeliveryPrice(deliveryPrice);
        order.setTotalPrice(subPrice.add(deliveryPrice));

        Order saved = orderRepository.save(order);
        orderHistoryService.createOrderHistory(new OrderHistoryCreateDto(saved.getId(), OrderStatus.CREATED));

        return ApiResponse.created("Order successfully created", orderMapper.toOrderResponseDto(saved));
    }
}
