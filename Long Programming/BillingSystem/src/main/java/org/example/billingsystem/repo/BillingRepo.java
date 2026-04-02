package org.example.billingsystem.repo;

import org.example.billingsystem.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepo extends JpaRepository<Billing, Long> {
}