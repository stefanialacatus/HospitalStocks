package com.example.demo.Statistics;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Statistics {
    private int statisticId;
    private int totalEntries;
    private int drugId;
    private BigDecimal avgDailyConsumption;
    private int totalConsumptions;
    private BigDecimal totalCosts;
    private String period;
}
