package t.com.onlinebooking.DHO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;
    @Column(name="user_name")
    private String userName;
    @Column(name="phone_no")
    private Long phoneNo;
    @Column(name="address")
    private String address;
    @Column(name="created_date")
    private LocalDate createdDate;
    @Column(name="city")
    private String city;
    @Column(name="discount")
    private Double discount;
}