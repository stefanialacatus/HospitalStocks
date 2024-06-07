package com.example.demo.DrugStock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/drugstock")
public class DrugStockController {

    @Autowired
    private DrugStockDAO drugStockDAO;

    @GetMapping("/getQuickReport")
    public ResponseEntity<Map<String, Object>> getQuickReport(@RequestParam("month") String month) {
        try {
            Map<String, Object> quickReportSummary = new HashMap<>();
            quickReportSummary.put("medicinesConsumed", drugStockDAO.getMedicinesConsumed(month));
            quickReportSummary.put("numberOfEntries", drugStockDAO.getNumberOfEntries(month));
            return ResponseEntity.ok(quickReportSummary);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
