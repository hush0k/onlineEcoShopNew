package kz.kbtu.onlineecoshopnew.user;


import kz.kbtu.onlineecoshopnew.common.exception.auth.HandleUserAlreadyExists;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleInvalidOldPassword;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleUserNotFound;
import kz.kbtu.onlineecoshopnew.common.exception.user.HandleUserNotFoundByEmail;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserCreateDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserPasswordChangeDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserRoleChangeDto;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserUpdateDto;
import kz.kbtu.onlineecoshopnew.user.dto.response.UserResponseDto;
import kz.kbtu.onlineecoshopnew.user.dto.response.UsersListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new HandleUserNotFound(userId)
        );
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new HandleUserNotFoundByEmail(email)
        );
    }

    public UserResponseDto createUser(UserCreateDto dto) {
        User user = userMapper.toUser(dto);
        User saved = userRepository.save(user);
        return userMapper.toUserResponse(saved);
    }

    public UserResponseDto updateUser(UserUpdateDto dto, Long userId) {
        User user = findUser(userId);
        String email = dto.email().strip().toLowerCase(Locale.ROOT);

        if (!email.equals(user.getEmail()) && isEmailExists(email)) {
            throw new HandleUserAlreadyExists(email);
        }

        userMapper.updateUserFromDto(dto, user);
        user.setEmail(email);
        userRepository.flush();
        return userMapper.toUserResponse(user);
    }

    public void updatePassword(UserPasswordChangeDto dto, String email) {
        User user = findUserByEmail(email);

        if (!passwordEncoder.matches(dto.oldPassword(), user.getHashedPassword())) {
            throw new HandleInvalidOldPassword();
        }

        user.setHashedPassword(passwordEncoder.encode(dto.newPassword()));
    }

    public UserResponseDto updateUserRole(UserRoleChangeDto dto, Long userId) {
        User user = findUser(userId);
        user.setUserRole(dto.userRole());
        userRepository.flush();
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(Long userId) {
        User user = findUser(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        return userMapper.toUserResponse(findUser(userId));
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
        return userMapper.toUserResponse(findUserByEmail(email));
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long userId, String email) {
        return userRepository.findById(userId)
                .map(user -> user.getEmail().equals(email))
                .orElse(false);
    }
}
