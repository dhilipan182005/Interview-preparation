package t.com.onlinebooking.DHO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
@Entity
@Getter
@Setter
@Table(name="report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    @ManyToOne
    @JoinColumn(name="orderId")
    private Order orderId;
    @ManyToOne
    @JoinColumn(name="userId")
    private User userId;
    @Column(name = "totalAmount")
    private Double totalAmount;
    @Column(name="reportDate")
    private LocalDate reportDate;
    @Column(name="status")
    private String status;
}