package kz.kbtu.onlineecoshopnew.order.order;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import kz.kbtu.onlineecoshopnew.order.order.dto.request.OrderCreateDto;
import kz.kbtu.onlineecoshopnew.order.order.dto.request.OrderStatusUpdateDto;
import kz.kbtu.onlineecoshopnew.order.order.dto.request.OrderUpdateDto;
import kz.kbtu.onlineecoshopnew.order.order.dto.response.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getMyOrders(@AuthenticationPrincipal String email,
                                                                           Pageable pageable) {
        return orderService.getMyOrders(email, pageable);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or @orderService.isOwner(#orderId, authentication.name)")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(@Valid @RequestBody OrderUpdateDto dto,
                                                                     @PathVariable Long orderId) {
        return orderService.updateOrder(orderId, dto);
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrderStatus(@Valid @RequestBody OrderStatusUpdateDto dto,
                                                                           @PathVariable Long orderId) {
        return orderService.updateStatusOfOrder(orderId, dto);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId) {
        return orderService.deleteOrder(orderId);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(@Valid @RequestBody OrderCreateDto dto,
                                                                     @AuthenticationPrincipal String email) {
        return orderService.createOrder(dto, email);
    }

    @PatchMapping("/{orderId}/next-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> moveToNextStatus(@PathVariable Long orderId) {
        return orderService.updateOrderStatus(orderId);
    }

    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @orderService.isOwner(#orderId, authentication.name)")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

}
