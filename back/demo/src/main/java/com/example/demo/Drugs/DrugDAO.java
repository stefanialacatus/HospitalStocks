package com.example.demo.Drugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DrugDAO {

    private static JdbcTemplate jdbcTemplate = null;

    @Autowired
    public DrugDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static List<Drugs> getAllDrugs() {
        String query = "SELECT * FROM drugs";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setId(rs.getInt("id"));
            drug.setName(rs.getString("name"));
            drug.setDosageForm(rs.getString("dosage_form"));
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
            drug.setDosageForm(rs.getString("dosage_form"));
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
            drug.setDosageForm(rs.getString("dosage_form"));
            drug.setPrice(rs.getBigDecimal("price"));
            return drug;
        });
    }
    public static int getCount() {
        String sql = "SELECT COUNT(*) FROM drugs";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public static List<Drugs> getDrugsInPage(int page) {
        // Call the stored procedure or function to get drugs for the given page number
        String sql = "SELECT * FROM get_drugs_info_in_page_table(?)";
        return jdbcTemplate.query(sql, new Object[]{page}, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setName(rs.getString("drug_name"));
            drug.setDosageForm(rs.getString("dosage_form"));
            drug.setIllness(rs.getString("illness"));
            drug.setStock(rs.getInt("stock"));
            System.out.println("the info:" + drug);
            return drug;
        });
    }
}
