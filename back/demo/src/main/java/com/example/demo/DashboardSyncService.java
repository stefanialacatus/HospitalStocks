package com.example.demo;

import com.example.demo.DrugStock.DrugStockDAO;
import com.example.demo.Drugs.DrugDAO;
import com.example.demo.Illness.IllnessDAO;
import com.example.demo.Patient.PatientDAO;
import com.example.demo.Supplier.SupplierDAO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardSyncService {

    @Autowired
    @Getter
    Map<String, Object> dashboardCached = new HashMap<>();

    private Thread syncThread;
    private boolean running = true;

    @PostConstruct
    public void startSyncThread() {
        syncThread = new Thread(() -> {
            while (running) {
                synchronizeDashData();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        syncThread.start();
    }

    @PreDestroy
    public void stopSyncThread() {
        running = false;
        if (syncThread != null) {
            syncThread.interrupt();
        }
    }

    private void synchronizeDashData() {
        Map<String, Object> inventorySumCached = new HashMap<>();
        inventorySumCached.put("status", DrugStockDAO.checkInventoryStatus());
        inventorySumCached.put("medicinesInStock", DrugDAO.getMedicinesAvailable());
        inventorySumCached.put("medicineShortage", DrugStockDAO.countShortageDrugs());
        dashboardCached.put("inventory", inventorySumCached);

        Map<String, Object> quickReportSumCached = new HashMap<>();
        quickReportSumCached.put("medicinesConsumed", DrugStockDAO.getMedicinesConsumed());
        quickReportSumCached.put("numberOfEntries", DrugStockDAO.getNumberOfEntries());
        dashboardCached.put("quickReport", quickReportSumCached);

        Map<String, Object> myHospitalSumCached = new HashMap<>();
        myHospitalSumCached.put("totalSuppliers", SupplierDAO.getCount());
        myHospitalSumCached.put("budget", DrugStockDAO.getBudget());
        dashboardCached.put("myHospital", myHospitalSumCached);

        Map<String, Object> patientsSumCached = new HashMap<>();
        patientsSumCached.put("totalPatients", PatientDAO.getCount());
        patientsSumCached.put("mostUsedMedicine", PatientDAO.getMostUsedMedicine());
        dashboardCached.put("patients", patientsSumCached);

        Map<String, Object> illnessSumCached = new HashMap<>();
        illnessSumCached.put("totalIllnesses", IllnessDAO.getCount());
        dashboardCached.put("illnesses", illnessSumCached);
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println("Dashboard data synchronized at " + currentTime.format(formatter));
    }

}
