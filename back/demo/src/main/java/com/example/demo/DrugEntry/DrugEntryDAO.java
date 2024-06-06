package com.example.demo.DrugEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.time.LocalDate;

@Component
public class DrugEntryDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DrugEntryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addDrugEntry(DrugEntry drugEntry, String drugName, String supplierName) {
        if (drugEntry == null) {
            throw new IllegalArgumentException("DrugEntry cannot be null");
        }

        // Generate a random batch number
        int batchNumber = generateRandomBatchNumber();
        drugEntry.setBatchNumber(String.valueOf(batchNumber));

        // Set the entry date to the current date
        drugEntry.setEntryDate(LocalDate.now());

        // Get the drugId using the drug name
        int drugId = getDrugIdByName(drugName);
        drugEntry.setDrugId(drugId);

        // Get the supplierId using the supplier name
        int supplierId = getSupplierIdByName(supplierName);
        drugEntry.setSupplierId(supplierId);

        // Insert the DrugEntry into the database
        insertDrugEntry(drugEntry);
    }

    private int generateRandomBatchNumber() {
        return (int) (Math.random() * 1000000);
    }

    private int getDrugIdByName(String drugName) {
        String sql = "SELECT id FROM drugs WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, drugName);
    }

    private int getSupplierIdByName(String supplierName) {
        String sql = "SELECT supplierid FROM supplier WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, supplierName);
    }

    private void insertDrugEntry(DrugEntry drugEntry) {
        System.out.println("In insert service : DrugEntry: " + drugEntry);
        String sql = "INSERT INTO drugentries (drugid, supplierid, batchnumber, quantity, entrydate) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, drugEntry.getDrugId());
            preparedStatement.setInt(2, drugEntry.getSupplierId());
            preparedStatement.setString(3, drugEntry.getBatchNumber());
            preparedStatement.setInt(4, drugEntry.getQuantity());
            preparedStatement.setDate(5, java.sql.Date.valueOf(drugEntry.getEntryDate()));
            System.out.println("In insert service : " + preparedStatement);
            return preparedStatement;
        });
    }
}
