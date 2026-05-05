package t.com.onlinebooking.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    @ManyToOne
    @JoinColumn(name="userId")
    private User userId;
    @ManyToOne
    @JoinColumn(name="productId")
    private Product productId;
    @Column(name="quantity")
    private Integer quantity;
}