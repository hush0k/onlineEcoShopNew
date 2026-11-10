package kz.kbtu.onlineecoshopnew.product.Category;

import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import kz.kbtu.onlineecoshopnew.common.exception.HandleAlreadyExists;
import kz.kbtu.onlineecoshopnew.common.exception.HandleNotFound;
import kz.kbtu.onlineecoshopnew.product.Category.dto.request.CategoryCreateDto;
import kz.kbtu.onlineecoshopnew.product.Category.dto.response.CategoriesListResponseDto;
import kz.kbtu.onlineecoshopnew.product.Category.dto.response.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private Category categoryIsExists(UUID categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new HandleNotFound("Category", categoryId)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<CategoriesListResponseDto>> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ApiResponse.ok("Categories successfully retrieved",
                categoryMapper.toCategoriesListResponseDto(categories, categories.size()));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategory(UUID categoryId) {
        Category category = categoryIsExists(categoryId);
        return ApiResponse.ok("Category successfully retrieved", categoryMapper.toCategoryResponseDto(category));
    }

    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(CategoryCreateDto categoryCreateDto) {
        if (categoryRepository.existsByName(categoryCreateDto.name())) {
            throw new HandleAlreadyExists("Category", "name", categoryCreateDto.name());
        }

        Category category = categoryMapper.toCategory(categoryCreateDto);
        categoryRepository.save(category);
        return ApiResponse.created("Category successfully created", categoryMapper.toCategoryResponseDto(category));
    }

    public ResponseEntity<ApiResponse<Void>> deleteCategory(UUID categoryId) {
        categoryIsExists(categoryId);
        categoryRepository.deleteById(categoryId);
        return ApiResponse.ok("Category successfully deleted");
    }

}
