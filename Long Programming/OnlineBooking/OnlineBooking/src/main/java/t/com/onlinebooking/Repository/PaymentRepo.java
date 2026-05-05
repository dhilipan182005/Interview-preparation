package t.com.onlinebooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t.com.onlinebooking.DHO.Payment;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
}