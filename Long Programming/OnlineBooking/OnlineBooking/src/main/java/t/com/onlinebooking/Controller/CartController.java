package t.com.onlinebooking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import t.com.onlinebooking.DHO.Cart;
import t.com.onlinebooking.Service.CartService;

import java.util.List;

@RestController
@RequestMapping("/Cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @GetMapping("/all")
    public List<Cart> getAllCart(){
        return cartService.getAllCart();
    }
    @PostMapping("/add")
    public String addToCart(@RequestBody Cart cart){
        return cartService.addToCart(cart);
    }
    @PutMapping("/{cartId}")
    public String updateCart(@PathVariable Long cartId,
                             @RequestBody Cart cart){
        return cartService.updateCart(cartId, cart);
    }
    @DeleteMapping("/{cartId}")
    public String deleteCart(@PathVariable Long cartId){
        return cartService.deleteCart(cartId);
    }
}