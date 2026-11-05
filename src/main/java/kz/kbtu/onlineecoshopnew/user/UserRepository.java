package kz.kbtu.onlineecoshopnew.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByEmailAndIsDeletedTrue(String email);
    List<User> findAllByIsDeletedTrueAndDeletedAtLessThanEqual(LocalDate date);
}
