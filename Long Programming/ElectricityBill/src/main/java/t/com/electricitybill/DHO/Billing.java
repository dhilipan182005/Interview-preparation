package t.com.electricitybill.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="Billing")
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int B_id;
    @Column(name="Amount")
    private double Amount;
    @Column(name="FineAmount")
    private double FineAmount;

    @Column(name = "UnitConsumed")
    private float UnitConsumed;
    @Column(name = "BillDate")
    private LocalDate billDate;
    @ManyToOne
    @JoinColumn(name = "ServiceNo")
    private Meter meter;
}
