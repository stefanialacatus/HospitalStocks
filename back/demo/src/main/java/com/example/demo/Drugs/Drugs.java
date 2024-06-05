package com.example.demo.Drugs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drugs {
    private int id;
    private BigDecimal price;
    private String name;
    private String dosageForm;
    private String unit;
}
