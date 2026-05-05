package t.com.onlinebooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t.com.onlinebooking.DHO.Cart;

public interface CartRepo extends JpaRepository<Cart, Long> {
}