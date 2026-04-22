package t.com.electricitybill.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.com.electricitybill.DHO.Customer;
import t.com.electricitybill.Repository.CustomerRepo;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepo cs;
    public List<Customer> getalldetails()
    {
        return cs.findAll();
    }
    public Customer postCust(Customer c)
    {
        return cs.save(c);
    }
    public Customer putCust(Customer c,int id)
    {
        Customer existing=cs.findById(id).orElseThrow(null);
        if(existing!=null)
        {
            existing.setC_Name(c.getC_Name());
            existing.setAddress(c.getAddress());
            existing.setPhoneNo(c.getPhoneNo());
            return cs.save(existing);
        }
        return null;
    }
    public String DeleteCust(int id)
    {
       if(cs.existsById(id))
       {
           cs.deleteById(id);
           return "Customer Deleted Successfully";
       }
       else {
           throw new RuntimeException("Customer Not Found");
       }
    }
}
