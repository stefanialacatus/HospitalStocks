package com.example.demo.DrugEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DrugEntryService {

    private final DrugEntryDAO drugEntryDAO;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DrugEntryService(DrugEntryDAO drugEntryDAO, JdbcTemplate jdbcTemplate) {
        this.drugEntryDAO = drugEntryDAO;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addDrugEntry(DrugEntry drugEntry, String drugName, String supplierName) {
        // Generate a random batch number
        int batchNumber = generateRandomBatchNumber();
        drugEntry.setBatchNumber(batchNumber);

        // Set the entry date to the current date
        drugEntry.setEntryDate(LocalDate.now());

        // Get the drugId using the drug name
        int drugId = getDrugIdByName(drugName);
        drugEntry.setDrugId(drugId);

        // Get the supplierId using the supplier name
        int supplierId = getSupplierIdByName(supplierName);
        drugEntry.setSupplierId(supplierId);

        // Insert the DrugEntry into the database
        insertDrugEntryIntoDatabase(drugEntry);
    }

    private int generateRandomBatchNumber() {
        // Implement logic to generate a random batch number
        return (int) (Math.random() * 1000000); // Example implementation
    }

    private int getDrugIdByName(String drugName) {
        String sql = "SELECT id FROM drugs WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, drugName);
    }

    private int getSupplierIdByName(String supplierName) {
        // Implement logic to get the supplierId using the supplier name
        // Example SQL query:
        String sql = "SELECT id FROM suppliers WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, supplierName);
    }

    private void insertDrugEntryIntoDatabase(DrugEntry drugEntry) {
        // Insert the DrugEntry into the database
        String sql = "INSERT INTO drugentries (drug_id, supplier_id, batch_number, quantity, entry_date, expiry_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                drugEntry.getDrugId(),
                drugEntry.getSupplierId(),
                drugEntry.getBatchNumber(),
                drugEntry.getQuantity(),
                drugEntry.getEntryDate(),
                drugEntry.getExpiryDate());
    }
}
