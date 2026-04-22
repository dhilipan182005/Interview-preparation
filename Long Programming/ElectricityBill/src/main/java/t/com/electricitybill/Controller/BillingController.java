package t.com.electricitybill.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import t.com.electricitybill.DHO.Billing;
import t.com.electricitybill.Service.BillingService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/Billing")
public class BillingController {
    @Autowired
    private BillingService bs;
    @GetMapping("/get")
    public List<Billing> getBillings(){
        return bs.getAllBills();
    }
    @PostMapping("/update")
    public Billing PostBill(@RequestBody Billing b) {
        return bs.PostBill(b);
    }
    @PutMapping("/{id}")
    public Billing PutBill(@RequestBody Billing b, @PathVariable int id){
        return bs.PutBill(id, b);
    }

    @DeleteMapping("/{id}")
    public String deleteBill(@PathVariable int id) {
        return bs.deleteBill(id);
    }

}
