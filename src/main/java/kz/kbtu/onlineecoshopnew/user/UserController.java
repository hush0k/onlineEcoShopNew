package kz.kbtu.onlineecoshopnew.user;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.kbtu.onlineecoshopnew.auth.AuthService;
import kz.kbtu.onlineecoshopnew.auth.dto.RegisterRequestDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserPasswordChangeDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserRoleChangeDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserUpdateDto;
import kz.kbtu.onlineecoshopnew.user.dto.response.UserResponseDto;
import kz.kbtu.onlineecoshopnew.user.dto.response.UsersListResponseDto;
import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> createUser(@Valid @RequestBody RegisterRequestDto dto) {
        return authService.createUserByAdmin(dto);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#userId, authentication.name)")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@Valid @RequestBody UserUpdateDto dto,
                                                                   @PathVariable Long userId) {
        return userService.updateUser(dto, userId);
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUserRole(@Valid @RequestBody UserRoleChangeDto dto,
                                                                       @PathVariable Long userId) {
        return userService.updateUserRole(dto, userId);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#userId, authentication.name)")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        return userService.deleteUserWrite(userId);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UsersListResponseDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMe(@AuthenticationPrincipal String email) {
        return userService.getUserByEmail(email);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@Valid @RequestBody UserPasswordChangeDto dto,
                                                            @AuthenticationPrincipal String email) {
        return userService.updatePassword(dto, email);
    }

    @PostMapping("/me/recover")
    public ResponseEntity<ApiResponse<UserResponseDto>> recoverUser(@AuthenticationPrincipal String email) {
        return userService.recoverUser(email);
    }

}
