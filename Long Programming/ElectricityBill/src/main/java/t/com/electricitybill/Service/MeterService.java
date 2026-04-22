package t.com.electricitybill.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.com.electricitybill.DHO.Customer;
import t.com.electricitybill.DHO.Meter;
import t.com.electricitybill.Repository.CustomerRepo;
import t.com.electricitybill.Repository.MeterRepo;

import java.util.List;

@Service
public class MeterService {
    @Autowired
    private MeterRepo mr;
    @Autowired
    private CustomerRepo cr;
    public List<Meter> GetMeter() {
        return mr.findAll();
    }
    public Meter PostMeter(Meter m){

        int customerId = m.getCustomer().getC_id();
        Customer customer = cr.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        m.setCustomer(customer);

        return mr.save(m);
    }

    public Meter PutMeter(Meter m, int id) {

        Meter existing = mr.findById(id)
                .orElseThrow(() -> new RuntimeException("Meter not found"));
        existing.setType(m.getType());
        existing.setUnit(m.getUnit());
        int customerId = m.getCustomer().getC_id();

        Customer customer = cr.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        existing.setCustomer(customer);
        return mr.save(existing);
    }
    public String DeleteMeter(int id){
        if(mr.existsById(id))
        {
            mr.deleteById(id);
            return "Meter deleted successfully";
        }
        else
        {
            throw  new RuntimeException("Meter not found");
        }
    }
}
