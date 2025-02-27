package com.example.demo.DrugStock;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DrugStock {
    private int stockId;
    private int drugId;
    private int quantity;
    private LocalDate lastUpdated;
}
