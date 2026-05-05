package t.com.onlinebooking.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @ManyToOne
    @JoinColumn(name="orderId")
    private Order orderId;
    @Column(name="amount")
    private Double amount;
    @Column(name="paymentDate")
    private LocalDate paymentDate;
    @Column(name="paymentType")
    private String paymentType;
    @Column(name="status")
    private String status;
}