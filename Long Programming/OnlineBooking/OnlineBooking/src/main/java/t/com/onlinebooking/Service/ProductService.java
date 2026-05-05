package t.com.onlinebooking.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import t.com.onlinebooking.DHO.Product;
import t.com.onlinebooking.Repository.ProductRepo;
import java.util.List;
@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;
    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
    public String addProduct(Product product){
        if(product.getAvailable() == null){
            product.setAvailable(true);
        }
        productRepo.save(product);
        return "Product added successfully";
    }
    public String updateProduct(Long productId, Product product){
        Product existingProduct = productRepo.findById(productId).orElse(null);
        if(existingProduct != null){
            existingProduct.setProductName(product.getProductName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setAvailable(product.getAvailable());
            productRepo.save(existingProduct);
            return "Product updated successfully";
        }
        return "Product not found";
    }
    public String deleteProduct(Long productId){
        if(productRepo.existsById(productId)){
            productRepo.deleteById(productId);
            return "Product deleted successfully";
        }
        return "Product not found";
    }
}