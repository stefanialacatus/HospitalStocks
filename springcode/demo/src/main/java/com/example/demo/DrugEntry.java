package com.example.demo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DrugEntry {
    private int entryId;
    private int drugId;
    private int supplierId;
    private int batchNumber;
    private int quantity;
    private LocalDate entryDate;
    private LocalDate expiryDate;
    private BigDecimal costPerUnit;
}
