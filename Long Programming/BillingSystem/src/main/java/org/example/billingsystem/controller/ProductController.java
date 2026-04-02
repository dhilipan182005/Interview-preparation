package org.example.billingsystem.controller;

import org.example.billingsystem.entity.Product;
import org.example.billingsystem.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService pro;

    public ProductController(ProductService pro) {
        this.pro = pro;
    }

    @GetMapping("/getproduct")
    public List<Product> getBooks() {
        return pro.getproduct();
    }

    @PostMapping("/postproduct")
    public Product addProduct(@RequestBody Product b) {
        return pro.addProduct(b);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable int id, @RequestBody Product b) {
        return pro.updateall(id, b);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) {
        return pro.deleteall(id);
    }
}