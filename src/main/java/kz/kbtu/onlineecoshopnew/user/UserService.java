package kz.kbtu.onlineecoshopnew.user;


import kz.kbtu.onlineecoshopnew.common.exception.auth.HandleUserAlreadyExists;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleInvalidOldPassword;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleUserAlreadyDeleted;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleUserNotDeleted;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleUserNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleUserNotFoundByEmail;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserCreateDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserPasswordChangeDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserRoleChangeDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserUpdateDto;
import kz.kbtu.onlineecoshopnew.user.dto.response.UserResponseDto;
import kz.kbtu.onlineecoshopnew.user.dto.response.UsersListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto createUser(UserCreateDto dto) {
        User user = userMapper.toUser(dto);
        User saved = userRepository.save(user);
        return userMapper.toUserResponse(saved);
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateDto dto, Long userId) {
        User user = getUserOrThrowException(userId);
        String email = dto.email().strip().toLowerCase(Locale.ROOT);

        if (!email.equals(user.getEmail()) && isEmailExists(email)) {
            throw new HandleUserAlreadyExists(email);
        }

        userMapper.updateUserFromDto(dto, user);
        user.setEmail(email);
        userRepository.flush();
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void updatePassword(UserPasswordChangeDto dto, String email) {
        User user = getUserByEmaiOrThrowException(email);

        if (!passwordEncoder.matches(dto.oldPassword(), user.getHashedPassword())) {
            throw new HandleInvalidOldPassword();
        }

        user.setHashedPassword(passwordEncoder.encode(dto.newPassword()));
    }

    @Transactional
    public UserResponseDto updateUserRole(UserRoleChangeDto dto, Long userId) {
        User user = getUserOrThrowException(userId);
        user.setUserRole(dto.userRole());
        userRepository.flush();
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void deleteUserWrite(Long userId) {
        User user = getUserOrThrowException(userId);
        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new HandleUserAlreadyDeleted(userId);
        }
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDate.now(ZoneId.of("Asia/Almaty")).plusMonths(3));
    }

    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Almaty")
    @Transactional
    public void deleteUsers() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Almaty"));
        List<User> users = userRepository.findAllByIsDeletedTrueAndDeletedAtLessThanEqual(today);

        userRepository.deleteAll(users);
    }

    @Transactional
    public UserResponseDto recoverUser(String email) {
        User user = getUserByEmaiOrThrowException(email);
        if (!Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new HandleUserNotDeleted(email);
        }
        user.setIsDeleted(false);
        user.setDeletedAt(null);
        return userMapper.toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        return userMapper.toUserResponse(getUserOrThrowException(userId));
    }

    @Transactional(readOnly = true)
    public UsersListResponseDto getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toUsersListResponseDto(users, users.size());
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        return userMapper.toUserResponse(getUserByEmaiOrThrowException(email));
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long userId, String email) {
        return userRepository.findById(userId)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new HandleUserNotFound(userId)
        );
    }

    private User getUserByEmaiOrThrowException(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new HandleUserNotFoundByEmail(email)
        );
    }
}
