package kz.kbtu.onlineecoshopnew.product.Product;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import kz.kbtu.onlineecoshopnew.product.Product.dto.request.ProductCreateDto;
import kz.kbtu.onlineecoshopnew.product.Product.dto.request.ProductFilter;
import kz.kbtu.onlineecoshopnew.product.Product.dto.request.ProductUpdateDto;
import kz.kbtu.onlineecoshopnew.product.Product.dto.response.ProductResponseDto;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(@Valid @RequestBody ProductCreateDto dto) {
        return ApiResponse.created("Product successfully created", productService.createProduct(dto));
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(@Valid @RequestBody ProductUpdateDto dto,
                                                                         @PathVariable UUID productId) {
        return ApiResponse.ok("Product successfully updated", productService.updateProduct(productId, dto));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return ApiResponse.ok("Product successfully deleted");
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProduct(@PathVariable UUID productId) {
        return ApiResponse.ok("Product successfully retrieved", productService.getProduct(productId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getProducts(ProductFilter filter, Pageable pageable) {
        return ApiResponse.ok("Products successfully retrieved", productService.getProducts(filter, pageable));
    }

}
