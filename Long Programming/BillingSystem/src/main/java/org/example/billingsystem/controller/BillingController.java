package org.example.billingsystem.controller;

import org.example.billingsystem.entity.Billing;
import org.example.billingsystem.service.BillingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    private final BillingService service;

    public BillingController(BillingService service) {
        this.service = service;
    }

    @GetMapping("/getbilling")
    public List<Billing> getAll() {
        return service.getAllBills();
    }

    @PostMapping("/postbilling")
    public Billing add(@RequestBody Billing b) {
        return service.addBill(b);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return service.deleteBill(id);
    }
}