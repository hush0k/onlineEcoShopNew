package kz.kbtu.onlineecoshopnew.product.Category;

import kz.kbtu.onlineecoshopnew.product.Category.dto.request.CategoryCreateDto;
import kz.kbtu.onlineecoshopnew.product.Category.dto.response.CategoriesListResponseDto;
import kz.kbtu.onlineecoshopnew.product.Category.dto.response.CategoryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryCreateDto dto);
    CategoryResponseDto toCategoryResponseDto(Category category);

    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "total", source = "total")
    CategoriesListResponseDto toCategoriesListResponseDto(List<Category> categories, int total);
}
