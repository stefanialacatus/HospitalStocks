package com.example.demo.DrugEntry;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Data
public class DrugEntryRequest {
    private String drugName;
    private int quantity;
    private String supplierName;
    public DrugEntryRequest(String name, int quantity, String supplierName) {
        this.drugName = name;
        this.quantity = quantity;
        this.supplierName = supplierName;
    }
}
