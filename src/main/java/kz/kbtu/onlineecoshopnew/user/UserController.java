package kz.kbtu.onlineecoshopnew.user;

import com.onlineShop.auth.AuthService;
import com.onlineShop.auth.dto.RegisterRequestDto;
import com.onlineShop.user.dto.request.UserPasswordChangeDto;
import com.onlineShop.user.dto.request.UserRoleChangeDto;
import com.onlineShop.user.dto.request.UserUpdateDto;
import com.onlineShop.user.dto.response.UserResponseDto;
import com.onlineShop.user.dto.response.UsersListResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void createUser(@Valid @RequestBody RegisterRequestDto dto) {
        authService.register(dto);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#userId, authentication.name)")
    public UserResponseDto updateUser(@Valid @RequestBody UserUpdateDto dto, @PathVariable Long userId) {
        return userService.updateUser(dto, userId);
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateUserRole(@Valid @RequestBody UserRoleChangeDto dto, @PathVariable Long userId) {
        return userService.updateUserRole(dto, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#userId, authentication.name)")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UsersListResponseDto getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/me")
    public UserResponseDto getMe(@AuthenticationPrincipal String email) {
        return userService.getUserByEmail(email);
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@Valid @RequestBody UserPasswordChangeDto dto, @AuthenticationPrincipal String email) {
        userService.updatePassword(dto, email);
    }

}
