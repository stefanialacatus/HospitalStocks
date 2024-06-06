package com.example.demo.DrugEntry;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class DrugEntry {

    private int drugId;
    private String supplierName;
    private String batchNumber;
    private int quantity;
    private LocalDate entryDate;
    private int id;
}
