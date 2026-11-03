package kz.kbtu.onlineecoshopnew.order.orderHistory;

import kz.kbtu.onlineecoshopnew.order.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    void deleteAllByOrder(Order order);
    List<OrderHistory> findByOrderId(Long orderId);
}
