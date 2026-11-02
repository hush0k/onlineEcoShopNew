package kz.kbtu.onlineecoshopnew.product.Category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateDto(

        @NotBlank(message = "Name can not be empty!")
        @Size(min=3, max=100, message = "Size of name should be more than 3 symbols and less than 100 symbols")
        String name
) {
}
