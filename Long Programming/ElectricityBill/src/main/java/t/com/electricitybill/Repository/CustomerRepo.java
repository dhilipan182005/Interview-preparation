package t.com.electricitybill.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t.com.electricitybill.DHO.Customer;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {
}
