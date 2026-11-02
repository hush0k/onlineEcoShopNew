package kz.kbtu.onlineecoshopnew.order.orderItems;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.request.OrderItemCreateDto;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.request.OrderItemUpdateDto;
import kz.kbtu.onlineecoshopnew.order.orderItems.dto.response.OrderItemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders/{orderId}/items")
@RequiredArgsConstructor
@Tag(name = "Order items", description = "Order items management")
public class OrderItemsController {

    private final OrderItemsService orderItemsService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderItemResponseDto>> createOrderItem(@Valid @RequestBody OrderItemCreateDto dto,
                                                                             @PathVariable Long orderId) {
        return ApiResponse.created("Order item successfully created", orderItemsService.createOrderItem(orderId, dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @orderService.isOwner(#orderId, authentication.name)")
    public ResponseEntity<ApiResponse<Page<OrderItemResponseDto>>> getOrderItems(@PathVariable Long orderId,
                                                                                 Pageable pageable) {
        return ApiResponse.ok("Order items successfully retrieved", orderItemsService.getOrderItems(orderId, pageable));
    }

    @GetMapping("/{orderItemId}")
    @PreAuthorize("hasRole('ADMIN') or @orderService.isOwner(#orderId, authentication.name)")
    public ResponseEntity<ApiResponse<OrderItemResponseDto>> getOrderItem(@PathVariable Long orderId,
                                                                          @PathVariable Long orderItemId) {
        return ApiResponse.ok("Order item successfully retrieved", orderItemsService.getOrderItem(orderId, orderItemId));
    }

    @PatchMapping("/{orderItemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderItemResponseDto>> updateOrderItem(@Valid @RequestBody OrderItemUpdateDto dto,
                                                                             @PathVariable Long orderId,
                                                                             @PathVariable Long orderItemId) {
        return ApiResponse.ok("Order item successfully updated",
                orderItemsService.updateOrderItem(orderId, orderItemId, dto));
    }

    @DeleteMapping("/{orderItemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteOrderItem(@PathVariable Long orderId,
                                                             @PathVariable Long orderItemId) {
        orderItemsService.deleteOrderItem(orderId, orderItemId);
        return ApiResponse.ok("Order item successfully deleted");
    }
}
