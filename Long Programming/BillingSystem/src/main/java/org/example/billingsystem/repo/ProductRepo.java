package org.example.billingsystem.repo;

import org.example.billingsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}