package kz.kbtu.onlineecoshopnew.order.order;


import kz.kbtu.onlineecoshopnew.common.exception.order.HandleOrderAlreadyCompleted;
import kz.kbtu.onlineecoshopnew.common.exception.order.HandleOrderNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.product.HandleProductNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.product.HandleProductsNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleUserNotFoundByEmail;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
                () -> new HandleOrderNotFound(orderId)
        );
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long orderId) {
        return orderMapper.toOrderResponseDto(orderIsExists(orderId));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toOrderResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getMyOrders(String email, Pageable pageable) {
        return orderRepository.findAllByUserEmail(email, pageable).map(orderMapper::toOrderResponseDto);
    }

    public OrderResponseDto updateOrder(Long orderId, OrderUpdateDto dto) {
        Order order = orderIsExists(orderId);
        orderMapper.updateOrderFromDto(dto, order);
        orderRepository.flush();
        return orderMapper.toOrderResponseDto(order);
    }

    public OrderResponseDto updateStatusOfOrder(Long orderId, OrderStatusUpdateDto dto) {
        Order order = orderIsExists(orderId);
        OrderStatus current = order.getStatus();

        if (!current.canTransitionTo(dto.status())) {
            throw new HandleOrderAlreadyCompleted(current);
        }

        order.setStatus(dto.status());
        orderHistoryRepository.save(
                OrderHistory.builder()
                        .order(order)
                        .status(dto.status())
                        .build()
        );
        return orderMapper.toOrderResponseDto(order);
    }

    public void deleteOrder(Long orderId) {
        Order order = orderIsExists(orderId);
        orderHistoryRepository.deleteAllByOrder(order);
        orderRepository.delete(order);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long orderId, String email) {
        return orderRepository.existsByIdAndUserEmail(orderId, email);
    }

    public OrderResponseDto updateOrderStatus(Long orderId) {
        Order order = orderIsExists(orderId);
        OrderStatus nextStatus = order.getStatus().next();
        order.setStatus(nextStatus);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDto(updatedOrder);
    }

    public OrderResponseDto cancelOrder(Long orderId) {
        Order order = orderIsExists(orderId);
        OrderStatus cancelStatus = order.getStatus().cancel();
        order.setStatus(cancelStatus);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDto(updatedOrder);
    }

    public OrderResponseDto createOrder(OrderCreateDto dto, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new HandleUserNotFoundByEmail(email));

        Set<UUID> ids = dto.items().stream()
                .map(OrderItemCreateDto::productId)
                .collect(Collectors.toSet());
        Map<UUID, Product> products = productService.getProductsById(ids).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));
        if (products.size() != ids.size()) {
            throw new HandleProductsNotFound(ids);
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

        return orderMapper.toOrderResponseDto(saved);
    }
}
