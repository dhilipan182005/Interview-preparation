package t.com.electricitybill.DHO;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int c_id;
    @Column(name="C_Name")
    private String C_Name;
    @Column(name="Address")
    private String Address;
    @Column(name="PhoneNo")
    private long PhoneNo;
}
