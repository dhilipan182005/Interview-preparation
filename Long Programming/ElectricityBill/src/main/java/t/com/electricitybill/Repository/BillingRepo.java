package t.com.electricitybill.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t.com.electricitybill.DHO.Billing;
import t.com.electricitybill.DHO.Customer;

public interface BillingRepo extends JpaRepository<Billing, Integer> {
}
