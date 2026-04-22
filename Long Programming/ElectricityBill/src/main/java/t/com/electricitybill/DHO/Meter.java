package t.com.electricitybill.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="Meter")
public class Meter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ServiceNo;
    @Column(name = "Unit")
    private float Unit;
    @Column(name = "Type")
    private String Type;
    @ManyToOne
    @JoinColumn(name = "c_id")
    private Customer customer;
}
