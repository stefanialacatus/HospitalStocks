package com.example.demo.DrugConsumption;

import com.example.demo.DrugEntry.DrugEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.SQLException;

public class DrugConsumptionDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DrugConsumptionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void consumeDrugEntry(DrugConsumption drugConsumption ,String drugName) throws SQLException {
        if (drugConsumption == null) {
            throw new IllegalArgumentException("DrugEntry cannot be null");
        }
        int drugId = getDrugIdByName(drugName);
        drugConsumption.setDrugId(drugId);
        insertDrugConsumption(drugConsumption);
    }

    void insertDrugConsumption(DrugConsumption drugConsumption) throws SQLException {
        System.out.println("In insert service : DrugConsumption: " + drugConsumption);
        String sql = "SELECT consume_drug_stock(?, ?, ?)";
        jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
            ps.setInt(1, drugConsumption.getDrugId());
            ps.setInt(2, drugConsumption.getQuantity());
            ps.execute();
            return null;
        });
    }
    private int getDrugIdByName(String drugName) throws SQLException {
            String sql = "SELECT id FROM drugs WHERE name = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, drugName);
    }
}
