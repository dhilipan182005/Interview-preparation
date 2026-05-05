package t.com.onlinebooking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import t.com.onlinebooking.DHO.Order;
import t.com.onlinebooking.Service.OrderService;
import java.util.List;

@RestController
@RequestMapping("/Order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/all")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @PostMapping("/add")
    public String addOrder(@RequestBody Order order){
        return orderService.addOrder(order);
    }

    @PutMapping("/{orderId}")
    public String updateOrder(@PathVariable Long orderId,@RequestBody Order order){
        return orderService.updateOrder(orderId,order);
    }

    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId){
        return orderService.deleteOrder(orderId);
    }
}