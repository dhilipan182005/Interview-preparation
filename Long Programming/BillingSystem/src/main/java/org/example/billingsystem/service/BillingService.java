package org.example.billingsystem.service;

import org.example.billingsystem.entity.Billing;
import org.example.billingsystem.repo.BillingRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingService {

    private final BillingRepo repo;

    public BillingService(BillingRepo repo) {
        this.repo = repo;
    }

    public List<Billing> getAllBills() {
        return repo.findAll();
    }

    public Billing addBill(Billing b) {
        return repo.save(b);
    }

    public String deleteBill(Long id) {
        repo.deleteById(id);
        return "Deleted successfully";
    }
}