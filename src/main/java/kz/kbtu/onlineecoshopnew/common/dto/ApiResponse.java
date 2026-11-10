package kz.kbtu.onlineecoshopnew.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        int status,
        String message,
        T data,
        Map<String, String> errors,
        Instant timestamp
) {

    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(true, status.value(), message, data, null, Instant.now());
    }

    public static ApiResponse<Void> success(HttpStatus status, String message) {
        return success(status, message, null);
    }

    public static ApiResponse<Void> error(HttpStatus status, String message) {
        return error(status, message, null);
    }

    public static ApiResponse<Void> error(HttpStatus status, String message, Map<String, String> errors) {
        return new ApiResponse<>(false, status.value(), message, null, errors, Instant.now());
    }


    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(success(HttpStatus.OK, message, data));
    }

    public static ResponseEntity<ApiResponse<Void>> ok(String message) {
        return ResponseEntity.ok(success(HttpStatus.OK, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(success(HttpStatus.CREATED, message, data));
    }

    public static ResponseEntity<ApiResponse<Void>> created(String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(success(HttpStatus.CREATED, message));
    }

    public static ResponseEntity<ApiResponse<Void>> errorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(error(status, message));
    }

    public static ResponseEntity<ApiResponse<Void>> errorResponse(HttpStatus status, String message,
                                                                  Map<String, String> errors) {
        return ResponseEntity.status(status).body(error(status, message, errors));
    }
}
