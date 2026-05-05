package t.com.onlinebooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t.com.onlinebooking.DHO.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
}