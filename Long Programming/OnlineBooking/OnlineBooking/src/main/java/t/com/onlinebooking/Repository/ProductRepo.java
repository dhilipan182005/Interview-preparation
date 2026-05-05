package t.com.onlinebooking.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import t.com.onlinebooking.DHO.Product;
public interface ProductRepo extends JpaRepository<Product, Long> {
}