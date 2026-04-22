package t.com.electricitybill.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.com.electricitybill.DHO.Billing;
import t.com.electricitybill.DHO.Meter;
import t.com.electricitybill.Repository.BillingRepo;
import t.com.electricitybill.Repository.MeterRepo;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillingService {

    @Autowired
    private BillingRepo br;

    @Autowired
    private MeterRepo mr;
    public List<Billing> getAllBills() {
        return br.findAll();
    }
    public Billing PostBill(Billing bill) {

        int serviceNo = bill.getMeter().getServiceNo();
        Meter meter = mr.findById(serviceNo)
                .orElseThrow(() -> new RuntimeException("Meter not found"));

        bill.setMeter(meter);
        if (bill.getBillDate() == null) {
            bill.setBillDate(LocalDate.now());
        }
        double amount = calculateBill(bill.getUnitConsumed());
        bill.setAmount(amount);
        double fine = calculateFine(bill.getBillDate());
        bill.setFineAmount(fine);

        return br.save(bill);
    }

    public Billing PutBill(int id, Billing bill) {

        Billing existing = br.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        existing.setUnitConsumed(bill.getUnitConsumed());

        double amount = calculateBill(bill.getUnitConsumed());
        existing.setAmount(amount);

        int serviceNo = bill.getMeter().getServiceNo();

        Meter meter = mr.findById(serviceNo)
                .orElseThrow(() -> new RuntimeException("Meter not found"));

        existing.setMeter(meter);

        if (bill.getBillDate() == null) {
            existing.setBillDate(LocalDate.now());
        } else {
            existing.setBillDate(bill.getBillDate());
        }
        double fine = calculateFine(existing.getBillDate());
        existing.setFineAmount(fine);

        return br.save(existing);
    }

    public String deleteBill(int id) {
        if (br.existsById(id)) {
            br.deleteById(id);
            return "Bill deleted successfully";
        } else {
            throw new RuntimeException("Bill not found");
        }
    }


    public double calculateBill(float units) {
        if (units <= 100) return units * 1.5;
        else if (units <= 200) return units * 2;
        else return units * 3;
    }


    public double calculateFine(LocalDate billDate) {

        if (billDate == null) return 0.0;

        if (billDate.isBefore(LocalDate.now().minusDays(10))) {
            return 50.0;
        }

        return 0.0;
    }
}