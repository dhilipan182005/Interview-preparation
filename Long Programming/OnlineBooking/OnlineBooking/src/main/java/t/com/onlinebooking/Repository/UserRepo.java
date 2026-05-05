package t.com.onlinebooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t.com.onlinebooking.DHO.User;
import java.time.LocalDate;

public interface UserRepo extends JpaRepository<User,Long>{
    long countByCreatedDate(LocalDate createdDate);
}