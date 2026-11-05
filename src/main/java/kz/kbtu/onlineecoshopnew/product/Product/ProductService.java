package kz.kbtu.onlineecoshopnew.product.Product;

import kz.kbtu.onlineecoshopnew.common.exception.HandleNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.product.HandleNotEnoughProductQuantity;
import kz.kbtu.onlineecoshopnew.product.Product.dto.request.ProductCreateDto;
import kz.kbtu.onlineecoshopnew.product.Product.dto.request.ProductFilter;
import kz.kbtu.onlineecoshopnew.product.Product.dto.request.ProductUpdateDto;
import kz.kbtu.onlineecoshopnew.product.Product.dto.response.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private Product productIsExists(UUID productId){
        return productRepository.findById(productId).orElseThrow(
                () -> new HandleNotFound("Product", productId)
        );
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(ProductFilter filter, Pageable pageable) {
        Specification<Product> spec = Specification.allOf(
                ProductSpecifications.hasCategory(filter.categoryId()),
                ProductSpecifications.priceGte(filter.minPrice()),
                ProductSpecifications.priceLte(filter.maxPrice()),
                ProductSpecifications.stockQuantityGte(filter.minStockQuantity()),
                ProductSpecifications.stockQuantityLte(filter.maxStockQuantity())
        );
        Page<Product> products = productRepository.findAllWithCategory(spec, pageable);
        return products.map(productMapper::toProductResponseDto);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProduct(UUID productId) {
        Product product = productIsExists(productId);
        return productMapper.toProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsById(Set<UUID> productIds) {
        return productRepository.findAllById(productIds);
    }

    public ProductResponseDto createProduct(ProductCreateDto productCreateDto) {
        Product product = productMapper.toProduct(productCreateDto);
        productRepository.save(product);
        return productMapper.toProductResponseDto(product);
    }

    public ProductResponseDto updateProduct(UUID productId, ProductUpdateDto productUpdateDto) {
        Product product = productIsExists(productId);
        productMapper.updateProductFromDto(productUpdateDto, product);
        return productMapper.toProductResponseDto(product);
    }

    public void deleteProduct(UUID productId) {
        productIsExists(productId);
        productRepository.deleteById(productId);
    }

    public void buyProduct(UUID productId, int quantity) {
        productIsExists(productId);
        int updated = productRepository.decreaseStock(productId, quantity);
        if (updated == 0) {
            throw new HandleNotEnoughProductQuantity(productId, quantity);
        }
    }

}
