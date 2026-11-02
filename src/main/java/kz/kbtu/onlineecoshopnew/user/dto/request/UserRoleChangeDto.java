package kz.kbtu.onlineecoshopnew.user.dto.request;

import com.onlineShop.user.UserRole;
import jakarta.validation.constraints.NotNull;

public record UserRoleChangeDto(

        @NotNull(message = "This field cannot be empty")
        UserRole userRole

) {}
