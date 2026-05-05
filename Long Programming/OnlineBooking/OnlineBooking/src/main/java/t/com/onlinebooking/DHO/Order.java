package t.com.onlinebooking.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @ManyToOne
    @JoinColumn(name="userId")
    private User userId;
    @ManyToOne
    @JoinColumn(name="productId")
    private Product productId;
    @Column(name="quantity")
    private Integer quantity;
    @Column(name="totalAmount")
    private Double totalAmount;
    @Column(name="orderDate")
    private LocalDate orderDate;
    @Column(name="status")
    private String status;
}