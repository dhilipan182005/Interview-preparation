package t.com.onlinebooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t.com.onlinebooking.DHO.Report;

public interface ReportRepo extends JpaRepository<Report, Long> {
}