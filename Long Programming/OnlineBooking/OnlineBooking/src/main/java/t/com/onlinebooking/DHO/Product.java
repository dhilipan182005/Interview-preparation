package t.com.onlinebooking.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name="price")
    private Double price;
    @Column(name="quantity")
    private Double quantity;
    @Column(name="category")
    private String category;
    @Column(name="available")
    private Boolean available;
}