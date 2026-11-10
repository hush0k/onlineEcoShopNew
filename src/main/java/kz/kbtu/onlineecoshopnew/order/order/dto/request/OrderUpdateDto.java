package kz.kbtu.onlineecoshopnew.order.order.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record OrderUpdateDto(

        @Size(max = 100, message = "Size of delivery city should be less than 100 symbols")
        String deliveryCity,

        @Size(max = 255, message = "Size of delivery address should be less than 255 symbols")
        String deliveryAddress,

        @Future(message = "Predicted date of delivery should be in the future")
        LocalDate predictedDateOfDelivery
) {
}
