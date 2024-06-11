package com.example.demo.DrugStock;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

@Repository
public class DrugStockDAO {
    private static JdbcTemplate jdbcTemplate = null;

    @Autowired
    public DrugStockDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static int getMedicinesConsumedPrediction() {
        String[] months = {"March", "April", "May", "June"};
        int[] medicinesConsumed = new int[months.length];

        for (int i = 0; i < months.length; i++) {
            medicinesConsumed[i] = getMedicinesConsumed(months[i]);
        }

        double averageIncrease = Arrays.stream(medicinesConsumed, 1, medicinesConsumed.length)
                .average()
                .orElse(0);

        return (int) (medicinesConsumed[months.length - 1] + averageIncrease);
    }

    public static int getNumberOfEntriesPrediction() {
        String[] months = {"March", "April", "May", "June"};
        int[] numberOfEntries = new int[months.length];

        for (int i = 0; i < months.length; i++) {
            numberOfEntries[i] = getNumberOfEntries(months[i]);
        }

        double averageIncrease = Arrays.stream(numberOfEntries, 1, numberOfEntries.length)
                .average()
                .orElse(0);

        return (int) (numberOfEntries[months.length - 1] + averageIncrease);
    }

    public Map<String, Map<String, Object>> drugsInPage(int pageNum) {
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
                double price = rs.getDouble("price");

                Map<String, Object> drugInfo = new HashMap<>();
                drugInfo.put("DosageForm", dosageForm);
                drugInfo.put("Illness", illness);
                drugInfo.put("Quantity", quantity);
                drugInfo.put("Price", price);

                drugsMap.put(drugName, drugInfo);
            }
            rs.close();
            stmt.close();
            return null;
        });
        return drugsMap;
    }

    public static String checkInventoryStatus() {
        String sql = "SELECT check_inventory_status()";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public static int getMedicinesConsumed() {
        String sql = "SELECT total_medicines_consumed(CURRENT_DATE)";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public static int getNumberOfEntries() {
        String sql = "SELECT calculate_medicines_added(CURRENT_DATE)";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public static int getNumberOfEntries(String month) {
        String sql = "SELECT COALESCE(calculate_medicines_added(TO_DATE(? || ' 2024', 'Month YYYY')), 0)";
        return jdbcTemplate.queryForObject(sql, Integer.class, month);
    }

    public static int getMedicinesConsumed(String month) {
        String sql = "SELECT COALESCE(total_medicines_consumed(TO_DATE(? || ' 2024', 'Month YYYY')), 0)";
        return jdbcTemplate.queryForObject(sql, Integer.class, month);
    }

    public static int getBudget() {
        String sql = "SELECT budget FROM hospital_info";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public static int countShortageDrugs() {
        String sql ="SELECT COALESCE(count_bad_drugs(), 0)";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
