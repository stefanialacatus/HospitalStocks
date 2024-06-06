package com.example.demo.DrugEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
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

        // Get the drugId using the drug name
        int drugId = getDrugIdByName(drugName);
        drugEntry.setDrugId(drugId);

        drugEntry.setSupplierName(supplierName);

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

    private void insertDrugEntry(DrugEntry drugEntry) {
        System.out.println("In insert service : DrugEntry: " + drugEntry);
        String sql = "SELECT add_drug_stock(?, ?, ?)";
        jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
            ps.setInt(1, drugEntry.getDrugId());
            ps.setInt(2, drugEntry.getQuantity());
            ps.setString(3, drugEntry.getSupplierName());
            ps.execute();
            return null;
        });
    }
}
