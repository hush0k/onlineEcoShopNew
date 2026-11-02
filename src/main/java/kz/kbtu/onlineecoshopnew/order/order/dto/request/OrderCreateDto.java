package kz.kbtu.onlineecoshopnew.order.order.dto.request;

import kz.kbtu.onlineecoshopnew.order.order.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderCreateDto(

        @NotNull(message = "Currency can not be empty!")
        Currency currency,

        @NotBlank(message = "Delivery city can not be empty!")
        @Size(max = 100, message = "Size of delivery city should be less than 100 symbols")
        String deliveryCity,

        @NotBlank(message = "Delivery address can not be empty!")
        @Size(max = 255, message = "Size of delivery address should be less than 255 symbols")
        String deliveryAddress
) {
}
