package kz.kbtu.onlineecoshopnew.auth;

import kz.kbtu.onlineecoshopnew.common.exception.HandleAlreadyExists;

import kz.kbtu.onlineecoshopnew.auth.dto.AuthResponseDto;
import kz.kbtu.onlineecoshopnew.auth.dto.LoginRequestDto;
import kz.kbtu.onlineecoshopnew.auth.dto.RefreshRequestDto;
import kz.kbtu.onlineecoshopnew.auth.dto.RegisterRequestDto;
import kz.kbtu.onlineecoshopnew.common.config.JwtProperties;
import kz.kbtu.onlineecoshopnew.common.dto.ApiResponse;
import kz.kbtu.onlineecoshopnew.common.exception.auth.HandleInvalidCredentials;
import kz.kbtu.onlineecoshopnew.common.exception.auth.HandleInvalidRefreshToken;
import kz.kbtu.onlineecoshopnew.user.User;
import kz.kbtu.onlineecoshopnew.user.UserService;
import kz.kbtu.onlineecoshopnew.user.dto.request.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProperties props;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthMapper authMapper;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ResponseEntity<ApiResponse<Void>> register(RegisterRequestDto dto) {
        createAccount(dto);
        return ApiResponse.created("User successfully registered");
    }

    @Transactional
    public ResponseEntity<ApiResponse<Void>> createUserByAdmin(RegisterRequestDto dto) {
        createAccount(dto);
        return ApiResponse.created("User successfully created");
    }

    private void createAccount(RegisterRequestDto dto) {
        String email = getEmail(dto.email());

        if (userService.isEmailExists(email)) {
            throw new HandleAlreadyExists("User", "email", email);
        }

        char[] hashedPassword;
        try {
            hashedPassword = passwordEncoder.encode(CharBuffer.wrap(dto.password())).toCharArray();
        } finally {
            Arrays.fill(dto.password(), '\0');
            Arrays.fill(dto.confirmPassword(), '\0');
        }

        UserCreateDto createDto = authMapper.toUserCreateDto(dto, email, hashedPassword);
        userService.createUser(createDto);
    }

    @Transactional
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(LoginRequestDto dto) {
        String email = getEmail(dto.email());

        User user = userService.findByEmail(email)
                .orElseThrow(HandleInvalidCredentials::new);

        boolean isPasswordMatches;
        try {
            isPasswordMatches = passwordEncoder.matches(CharBuffer.wrap(dto.password()),
                    new String(user.getHashedPassword()));
        } finally {
            Arrays.fill(dto.password(), '\0');
        }

        if (!isPasswordMatches) {
            throw new HandleInvalidCredentials();
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = createRefreshToken(user);

        return ApiResponse.ok("Login successful", new AuthResponseDto(accessToken, refreshToken));
    }

    @Transactional(noRollbackFor = HandleInvalidRefreshToken.class)
    public ResponseEntity<ApiResponse<AuthResponseDto>> refresh(RefreshRequestDto dto) {
        RefreshToken storedToken = refreshTokenRepository.findByTokenHash(hashToken(dto.refreshToken()))
                .orElseThrow(HandleInvalidRefreshToken::new);

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new HandleInvalidRefreshToken();
        }

        User user = storedToken.getUser();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = createRefreshToken(user);

        return ApiResponse.ok("Token successfully refreshed", new AuthResponseDto(accessToken, refreshToken));
    }

    private String createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hashToken(token));
        refreshToken.setExpiresAt(Instant.now().plus(props.refreshTtl()));
        refreshTokenRepository.save(refreshToken);

        return token;
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private String getEmail(String email){
        return email.strip().toLowerCase(Locale.ROOT);
    }

}
