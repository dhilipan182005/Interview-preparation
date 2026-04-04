package org.example.billingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillingResponse {

    private Long billingId;
    private String billerName;
    private String userName;
    private String productName;
    private Double price;
    private Double totalPrice;
}