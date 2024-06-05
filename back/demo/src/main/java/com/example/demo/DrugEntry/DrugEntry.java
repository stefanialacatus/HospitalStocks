package com.example.demo.DrugEntry;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Data
public class DrugEntry {
    private int entryId;
    private int drugId;
    private int supplierId;
    private int batchNumber;
    private int quantity;
    private LocalDate entryDate;
    private LocalDate expiryDate;

}
