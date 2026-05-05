package t.com.onlinebooking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import t.com.onlinebooking.DHO.Product;
import t.com.onlinebooking.Service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/Product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/all")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }
    @PostMapping("/add")
    public String addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
    @PutMapping("/{productId}")
    public String updateProduct(@PathVariable Long productId,
                                @RequestBody Product product){
        return productService.updateProduct(productId, product);
    }
    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable Long productId){
        return productService.deleteProduct(productId);
    }
}