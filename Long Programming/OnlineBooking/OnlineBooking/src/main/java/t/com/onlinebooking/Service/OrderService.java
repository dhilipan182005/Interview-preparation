package t.com.onlinebooking.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.com.onlinebooking.DHO.Order;
import t.com.onlinebooking.Repository.OrderRepo;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }

    public String addOrder(Order order){
        order.setOrderDate(LocalDate.now());
        order.setStatus("PLACED");
        orderRepo.save(order);
        return "Order placed successfully";
    }

    public String updateOrder(Long orderId, Order order){
        Order existingOrder = orderRepo.findById(orderId).orElse(null);
        if(existingOrder != null){
            existingOrder.setUserId(order.getUserId());
            existingOrder.setProductId(order.getProductId());
            existingOrder.setQuantity(order.getQuantity());
            existingOrder.setTotalAmount(order.getTotalAmount());
            existingOrder.setStatus(order.getStatus());
            orderRepo.save(existingOrder);
            return "Order updated successfully";
        }
        return "Order not found";
    }

    public String deleteOrder(Long orderId){
        if(orderRepo.existsById(orderId)){
            orderRepo.deleteById(orderId);
            return "Order deleted successfully";
        }
        return "Order not found";
    }
}