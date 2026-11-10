package kz.kbtu.onlineecoshopnew.product.Product.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductFilter(
        UUID categoryId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer minStockQuantity,
        Integer maxStockQuantity
) {}
