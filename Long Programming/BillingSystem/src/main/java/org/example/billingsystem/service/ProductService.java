package org.example.billingsystem.service;

import org.example.billingsystem.entity.Product;
import org.example.billingsystem.repo.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo repo;

    public ProductService(ProductRepo repo) {
        this.repo = repo;
    }

    public List<Product> getproduct() {
        return repo.findAll();
    }

    public Product addProduct(Product p) {
        return repo.save(p);
    }

    public Product updateall(int id, Product p) {
        return repo.findById((long) id).map(existing -> {
            existing.setProductName(p.getProductName());
            existing.setQuantity(p.getQuantity());
            return repo.save(existing);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public String deleteall(int id) {
        repo.deleteById((long) id);
        return "Deleted successfully";
    }
}