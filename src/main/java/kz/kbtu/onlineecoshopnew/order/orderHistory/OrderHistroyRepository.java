package kz.kbtu.onlineecoshopnew.order.orderHistory;

import kz.kbtu.onlineecoshopnew.order.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistroyRepository extends JpaRepository<OrderHistory, Long> {

    void deleteAllByOrder(Order order);
}
