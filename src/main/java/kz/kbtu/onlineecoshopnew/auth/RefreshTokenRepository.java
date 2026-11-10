package kz.kbtu.onlineecoshopnew.auth;

import kz.kbtu.onlineecoshopnew.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUser(User user);

    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
