package com.example.demo.DrugStock;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.HashMap;

@Repository
public class DrugStockDAO {
    private static JdbcTemplate jdbcTemplate = null;
    @Autowired
    public DrugStockDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static Map<String, Map<String, Object>> drugsInPage(int pageNum) {
        Map<String, Map<String, Object>> drugsMap = new HashMap<>();
        jdbcTemplate.execute((ConnectionCallback<Object>) con -> {
            CallableStatement stmt = con.prepareCall("{ ? = call get_drugs_in_page(?) }");
            stmt.registerOutParameter(1, Types.OTHER);
            stmt.setInt(2, pageNum);
            stmt.execute();
            ResultSet rs = (ResultSet) stmt.getObject(1);
            while (rs.next()) {
                String drugName = rs.getString("drug_name");
                String dosageForm = rs.getString("dosage_form");
                String illness = rs.getString("illness");
                int quantity = rs.getInt("quantity");

                Map<String, Object> drugInfo = new HashMap<>();
                drugInfo.put("DosageForm", dosageForm);
                drugInfo.put("Illness", illness);
                drugInfo.put("Quantity", quantity);

                drugsMap.put(drugName, drugInfo);
            }
            rs.close();
            stmt.close();
            return null;
        });
        return drugsMap;
    }
    public static String checkInventoryStatus() {
        // Call the stored function to check inventory status
        String sql = "SELECT check_inventory_status()";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
    public static int getBudget() {
        // Call the stored function to get the budget
        String sql = "select budget from hospital_info";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public static int checkMedicinesInStock(){
        // Call the stored function to check the number of medicines in stock
        String sql = "SELECT drugs_in_stock()";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public static int getNumberOfEntries(){
        String sql = "SELECT calculate_medicines_added(CURRENT_DATE)";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public static int getMedicinesConsumed(){
        String sql = "SELECT total_medicines_consumed(CURRENT_DATE)";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
