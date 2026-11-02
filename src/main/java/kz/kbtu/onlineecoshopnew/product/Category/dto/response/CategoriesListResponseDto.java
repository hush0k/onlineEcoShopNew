package kz.kbtu.onlineecoshopnew.product.Category.dto.response;

import java.util.List;

public record CategoriesListResponseDto(
        List<CategoryResponseDto> categories,
        int total
) {}
