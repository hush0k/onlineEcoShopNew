package kz.kbtu.onlineecoshopnew.order.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUserEmail(String email, Pageable pageable);

    boolean existsByIdAndUserEmail(Long id, String email);
}
