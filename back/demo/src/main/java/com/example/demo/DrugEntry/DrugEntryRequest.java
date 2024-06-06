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
    private String supplierName;
    private int quantity;
}
