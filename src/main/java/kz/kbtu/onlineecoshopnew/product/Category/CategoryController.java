package kz.kbtu.onlineecoshopnew.product.Category;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import kz.kbtu.onlineecoshopnew.product.Category.dto.request.CategoryCreateDto;
import kz.kbtu.onlineecoshopnew.product.Category.dto.response.CategoriesListResponseDto;
import kz.kbtu.onlineecoshopnew.product.Category.dto.response.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(@Valid @RequestBody CategoryCreateDto dto) {
        return ApiResponse.created("Category successfully created", categoryService.createCategory(dto));
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.ok("Category successfully deleted");
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategory(@PathVariable UUID categoryId) {
        return ApiResponse.ok("Category successfully retrieved", categoryService.getCategory(categoryId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CategoriesListResponseDto>> getCategories() {
        return ApiResponse.ok("Categories successfully retrieved", categoryService.getCategories());
    }

}
