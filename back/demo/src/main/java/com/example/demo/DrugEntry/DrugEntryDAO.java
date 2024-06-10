package com.example.demo.DrugEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DrugEntryDAO {

    private static JdbcTemplate jdbcTemplate = null;

    @Autowired
    public DrugEntryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void importData(MultipartFile file) throws Exception {
        List<DrugEntry> entries = new ArrayList<>();
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = br.readLine().split(csvSplitBy);

            while ((line = br.readLine()) != null) {
                DrugEntry entry = new DrugEntry();
                String[] data = line.split(csvSplitBy);
                String name = data[0];
                int quantity = Integer.parseInt(data[1]);
                String supplierName = data[2];
                int drugId = getDrugIdByName(name);
                entry.setDrugId(drugId);
                entry.setQuantity(quantity);
                entry.setSupplierName(supplierName);
                entries.add(entry);
            }
        }

        for (DrugEntry entry : entries) {
            insertDrugEntry(entry);
        }
    }

    public void addDrugEntry(DrugEntry drugEntry, String drugName, String supplierName) throws SQLException {
        if (drugEntry == null) {
            throw new IllegalArgumentException("DrugEntry cannot be null");
        }
        int drugId = getDrugIdByName(drugName);
        drugEntry.setDrugId(drugId);

        drugEntry.setSupplierName(supplierName);
        insertDrugEntry(drugEntry);
    }

    private int generateRandomBatchNumber() {
        return (int) (Math.random() * 1000000);
    }

    private static int getDrugIdByName(String drugName) throws SQLException {
        String sql = "SELECT id FROM drugs WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, drugName);
    }

    private static void insertDrugEntry(DrugEntry drugEntry) throws SQLException{
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
