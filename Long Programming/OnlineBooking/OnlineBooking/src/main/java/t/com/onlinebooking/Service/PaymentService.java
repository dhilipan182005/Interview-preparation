package t.com.onlinebooking.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.com.onlinebooking.DHO.Payment;
import t.com.onlinebooking.Repository.PaymentRepo;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    public List<Payment> getAllPayments(){
        return paymentRepo.findAll();
    }

    public String addPayment(Payment payment){
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus("SUCCESS");
        paymentRepo.save(payment);
        return "Payment successful";
    }

    public String updatePayment(Long paymentId, Payment payment){
        Payment existingPayment = paymentRepo.findById(paymentId).orElse(null);
        if(existingPayment != null){
            existingPayment.setOrderId(payment.getOrderId());
            existingPayment.setAmount(payment.getAmount());
            existingPayment.setPaymentType(payment.getPaymentType());
            existingPayment.setStatus(payment.getStatus());
            paymentRepo.save(existingPayment);
            return "Payment updated successfully";
        }
        return "Payment not found";
    }

    public String deletePayment(Long paymentId){
        if(paymentRepo.existsById(paymentId)){
            paymentRepo.deleteById(paymentId);
            return "Payment deleted successfully";
        }
        return "Payment not found";
    }
}