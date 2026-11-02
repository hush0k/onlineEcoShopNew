package kz.kbtu.onlineecoshopnew.user.dto.request;

import jakarta.validation.constraints.NotNull;
import kz.kbtu.onlineecoshopnew.user.UserRole;

public record UserRoleChangeDto(

        @NotNull(message = "This field cannot be empty")
        UserRole userRole

) {}
