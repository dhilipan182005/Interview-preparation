package t.com.onlinebooking.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.com.onlinebooking.DHO.Cart;
import t.com.onlinebooking.Repository.CartRepo;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;
    public List<Cart> getAllCart(){
        return cartRepo.findAll();
    }
    public String addToCart(Cart cart){
        cartRepo.save(cart);
        return "Product added to cart";
    }
    public String updateCart(Long cartId, Cart cart){
        Cart existingCart = cartRepo.findById(cartId).orElse(null);
        if(existingCart != null){
            existingCart.setUserId(cart.getUserId());
            existingCart.setProductId(cart.getProductId());
            existingCart.setQuantity(cart.getQuantity());
            cartRepo.save(existingCart);
            return "Cart updated successfully";
        }
        return "Cart item not found";
    }
    public String deleteCart(Long cartId){
        if(cartRepo.existsById(cartId)){
            cartRepo.deleteById(cartId);
            return "Cart item deleted successfully";
        }
        return "Cart item not found";
    }
}