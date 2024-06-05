package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/summary")
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> dashboardSummary = new HashMap<>();

        Map<String, Object> inventorySummary = new HashMap<>();
        inventorySummary.put("status", "Good");
        inventorySummary.put("medicinesInStock", 152);
        inventorySummary.put("medicineShortage", false);
        dashboardSummary.put("inventory", inventorySummary);

        Map<String, Object> quickReportSummary = new HashMap<>();
        quickReportSummary.put("medicinesConsumed", 1856);
        quickReportSummary.put("numberOfEntries", 5288);
        dashboardSummary.put("quickReport", quickReportSummary);

        Map<String, Object> myHospitalSummary = new HashMap<>();
        myHospitalSummary.put("totalSuppliers", 10);
        dashboardSummary.put("myHospital", myHospitalSummary);

        Map<String, Object> patientsSummary = new HashMap<>();
        patientsSummary.put("totalPatients", 845);
        patientsSummary.put("mostUsedMedicine", "Paracetamol");
        dashboardSummary.put("patients", patientsSummary);

        return dashboardSummary;
    }
}
