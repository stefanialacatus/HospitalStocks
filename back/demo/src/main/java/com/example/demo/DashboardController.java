package com.example.demo;

import com.example.demo.DrugStock.DrugStockDAO;
import com.example.demo.Drugs.DrugDAO;
import com.example.demo.Illness.IllnessDAO;
import com.example.demo.Patient.PatientDAO;
import com.example.demo.Supplier.SupplierDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardSyncService dashboardSync;

    @GetMapping("/summary")
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> dashboardSummary = new HashMap<>();

        Map<String, Object> inventorySummary = new HashMap<>();
        inventorySummary.put("status", DrugStockDAO.checkInventoryStatus());
        inventorySummary.put("medicinesInStock", DrugDAO.getMedicinesAvailable());
        inventorySummary.put("medicineShortage", DrugStockDAO.countShortageDrugs());
        dashboardSummary.put("inventory", inventorySummary);

        Map<String, Object> quickReportSummary = new HashMap<>();
        quickReportSummary.put("medicinesConsumed", DrugStockDAO.getMedicinesConsumed());
        quickReportSummary.put("numberOfEntries", DrugStockDAO.getNumberOfEntries());
        dashboardSummary.put("quickReport", quickReportSummary);

        Map<String, Object> myHospitalSummary = new HashMap<>();
        myHospitalSummary.put("totalSuppliers", SupplierDAO.getCount());
        myHospitalSummary.put("budget", DrugStockDAO.getBudget());
        dashboardSummary.put("myHospital", myHospitalSummary);

        Map<String, Object> patientsSummary = new HashMap<>();
        patientsSummary.put("totalPatients", PatientDAO.getCount());
        patientsSummary.put("mostUsedMedicine", PatientDAO.getMostUsedMedicine());
        dashboardSummary.put("patients", patientsSummary);

        Map<String, Object> illnessSummary = new HashMap<>();
        illnessSummary.put("totalIllnesses", IllnessDAO.getCount());
        dashboardSummary.put("illnesses", illnessSummary);
        return dashboardSummary;
    }
    @GetMapping("/monthStatistic")
    public List<Map<String, Object>> getMonthStatistic() {
        List<Map<String, Object>> monthlyStatistics = new ArrayList<>();
        String[] months = {"March", "April", "May", "June", "Next Month"};

        for (String month : months) {
            if(!month.equals("Next Month")) {
                Map<String, Object> monthlyData = new HashMap<>();
                monthlyData.put("month", month);
                monthlyData.put("MedsConsumed", DrugStockDAO.getMedicinesConsumed(month));
                monthlyData.put("NoEntries", DrugStockDAO.getNumberOfEntries(month));
                monthlyStatistics.add(monthlyData);
            }
            else {
                Map<String, Object> predictions = new HashMap<>();
                predictions.put("month", month);
                predictions.put("MedsConsumed", DrugStockDAO.getMedicinesConsumedPrediction());
                predictions.put("NoEntries", DrugStockDAO.getNumberOfEntriesPrediction());
                monthlyStatistics.add(predictions);
            }
        }
        System.out.println(monthlyStatistics);

        return monthlyStatistics;
    }
    @GetMapping("/getSynchronizedDashboard")
    public Map<String, Object> getSynchronizedDashboard() {
        return dashboardSync.getDashboardCached();
    }
}