package com.example.demo.Drugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugDAO {

    static JdbcTemplate jdbcTemplate = null;

    // Select All Drugs
    public static List<Drugs> getAllDrugs() {
        String query = "SELECT * FROM drugs";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setId(rs.getInt("id"));
            drug.setName(rs.getString("name"));
            drug.setDosageForm(rs.getString("dosage_form")); // Ensure correct column name
            drug.setPrice(rs.getBigDecimal("price"));
            return drug;
        });
    }

    public static Drugs findById(int id) {
        String query = "SELECT * FROM drugs WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setId(rs.getInt("id"));
            drug.setName(rs.getString("name"));
            drug.setDosageForm(rs.getString("dosage_form")); // Ensure correct column name
            drug.setPrice(rs.getBigDecimal("price"));
            return drug;
        });
    }

    public static List<Drugs> findByName(String name) {
        String query = "SELECT * FROM drugs WHERE name LIKE ?";
        return jdbcTemplate.query(query, new Object[]{"%" + name + "%"}, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setId(rs.getInt("id"));
            drug.setName(rs.getString("name"));
            drug.setDosageForm(rs.getString("dosage_form")); // Ensure correct column name
            drug.setPrice(rs.getBigDecimal("price"));
            return drug;
        });
    }
}
