package kz.kbtu.onlineecoshopnew.order.orderItems;

import kz.kbtu.onlineecoshopnew.order.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrederItems, Long> {

    @EntityGraph(attributePaths = {"product"})
    Page<OrederItems> findAllByOrderId(Long orderId, Pageable pageable);

    @EntityGraph(attributePaths = {"product"})
    Optional<OrederItems> findByIdAndOrderId(Long id, Long orderId);

    void deleteAllByOrder(Order order);
}
