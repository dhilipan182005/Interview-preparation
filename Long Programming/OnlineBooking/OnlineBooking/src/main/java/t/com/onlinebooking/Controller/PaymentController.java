package t.com.onlinebooking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import t.com.onlinebooking.DHO.Payment;
import t.com.onlinebooking.Service.PaymentService;
import java.util.List;

@RestController
@RequestMapping("/Payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/all")
    public List<Payment> getAllPayments(){
        return paymentService.getAllPayments();
    }

    @PostMapping("/add")
    public String addPayment(@RequestBody Payment payment){
        return paymentService.addPayment(payment);
    }

    @PutMapping("/{paymentId}")
    public String updatePayment(@PathVariable Long paymentId,@RequestBody Payment payment){
        return paymentService.updatePayment(paymentId,payment);
    }

    @DeleteMapping("/{paymentId}")
    public String deletePayment(@PathVariable Long paymentId){
        return paymentService.deletePayment(paymentId);
    }
}