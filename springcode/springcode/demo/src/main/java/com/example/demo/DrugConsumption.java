package com.example.demo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DrugConsumption {
    private int consumptionId;
    private int drugId;
    private int quantity;
    private LocalDate consumptionDate;
}
