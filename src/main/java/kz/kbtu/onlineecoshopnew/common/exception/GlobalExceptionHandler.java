package kz.kbtu.onlineecoshopnew.common.exception;


import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import kz.kbtu.onlineecoshopnew.common.exception.auth.HandleInvalidCredentials;
import kz.kbtu.onlineecoshopnew.common.exception.auth.HandleInvalidRefreshToken;
import kz.kbtu.onlineecoshopnew.common.exception.auth.HandleUserAlreadyExists;
import kz.kbtu.onlineecoshopnew.common.exception.category.HandleCategoryAlreadyExists;
import kz.kbtu.onlineecoshopnew.common.exception.category.HandleCategoryNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.order.HandleOrderAlreadyCompleted;
import kz.kbtu.onlineecoshopnew.common.exception.order.HandleOrderCanNotBeCancled;
import kz.kbtu.onlineecoshopnew.common.exception.order.HandleOrderNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.orderItems.HandleOrderItemNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.product.HandleNotEnoughProductQuantity;
import kz.kbtu.onlineecoshopnew.common.exception.product.HandleProductNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.product.HandleProductsNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleInvalidOldPassword;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleUserNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleUserNotFoundByEmail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({HandleUserNotFound.class, HandleUserNotFoundByEmail.class,
            HandleProductNotFound.class, HandleCategoryNotFound.class, HandleOrderNotFound.class,
            HandleOrderItemNotFound.class, HandleProductsNotFound.class,})
    public ResponseEntity<ApiResponse<Void>> handleNotFound(RuntimeException exception) {
        return ApiResponse.errorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(HandleInvalidOldPassword.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidOldPassword(HandleInvalidOldPassword exception) {
        return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({HandleUserAlreadyExists.class, HandleCategoryAlreadyExists.class,
            HandleOrderAlreadyCompleted.class})
    public ResponseEntity<ApiResponse<Void>> handleConflict(RuntimeException exception) {
        return ApiResponse.errorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(HandleNotEnoughProductQuantity.class)
    public ResponseEntity<ApiResponse<Void>> handleNotEnoughProductQuantity(HandleNotEnoughProductQuantity exception) {
        return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({HandleInvalidCredentials.class, HandleInvalidRefreshToken.class})
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(RuntimeException exception) {
        return ApiResponse.errorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException exception) {
        return ApiResponse.errorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(HandleOrderCanNotBeCancled.class)
    public ResponseEntity<ApiResponse<Void>> handleOrderCanNotBeCancled(HandleOrderCanNotBeCancled exception) {
        return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}
