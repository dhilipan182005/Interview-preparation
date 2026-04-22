package t.com.electricitybill.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import t.com.electricitybill.DHO.Customer;
import t.com.electricitybill.Service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/Customer")
public class CustomerController {
    @Autowired
    private CustomerService cs;

    @GetMapping("/details")
    public List<Customer> getalldetails(){
        return cs.getalldetails();
    }

    @PostMapping("/update")
    public Customer postCust(@RequestBody Customer c){
        return cs.postCust(c);
    }
    @PutMapping("/{id}")
    public Customer putCust(@RequestBody Customer c, @PathVariable int id){
        return cs.putCust(c,id);
    }

    @DeleteMapping("/{id}")
    public String DeleteCust(@PathVariable int id){
        return cs.DeleteCust(id);
    }
}
