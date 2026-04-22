package t.com.electricitybill.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t.com.electricitybill.DHO.Meter;

public interface MeterRepo extends JpaRepository<Meter,Integer> {
}
