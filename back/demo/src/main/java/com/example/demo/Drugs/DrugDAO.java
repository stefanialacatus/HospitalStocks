package com.example.demo.Drugs;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class DrugDAO {
    @Setter
    private static Integer filter = 0;

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
        String query = "SELECT d.name AS name," +
                " d.dosage_form AS dosage_form ," +
                " (SELECT i.name FROM illnesses i JOIN illness_drug id ON i.id = id.illness_id WHERE id.drug_id = d.id LIMIT 1) AS illness, " +
                "d.stock AS stock FROM drugs d WHERE name LIKE ?";
        return jdbcTemplate.query(query, new Object[]{"%" + name + "%"}, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setName(rs.getString("name"));
            drug.setDosageForm(rs.getString("dosage_form"));
            drug.setIllness(rs.getString("illness"));
            drug.setStock(rs.getInt("stock"));
            return drug;
        });
    }
    public static int getCount() {
        String sql = "SELECT COUNT(*) FROM drugs";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public static int getMedicinesAvailable() {
        String sql = "SELECT COUNT(*) FROM drugs where stock > 0";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public static List<Drugs> getAllDrugsFiltered(int page) {
        int pageSize = 8;
        String sql = "SELECT * FROM drugs_info_no_page()";
        List<Drugs> allDrugs = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setName(rs.getString("drug_name"));
            drug.setDosageForm(rs.getString("dosage_form"));
            drug.setIllness(rs.getString("illness"));
            drug.setStock(rs.getInt("stock"));
            System.out.println("the info:" + drug);
            return drug;
        });
        return getDrugsInPage(allDrugs, page, pageSize);
    }

    public static List<Drugs> getDrugsInPage(List<Drugs> allDrugs, int page, int pageSize) {
        List<Drugs> filteredDrugs = allDrugs;
        System.out.println("FILTER " + filter);
        if (filter == 1) {
            filteredDrugs = allDrugs.stream()
                    .sorted(Comparator.comparing(Drugs::getName))
                    .collect(Collectors.toList());
        } else if (filter == 2) {
            filteredDrugs = allDrugs.stream()
                    .sorted(Comparator.comparing(Drugs::getName).reversed())
                    .collect(Collectors.toList());
        } else if (filter == 3) {
            filteredDrugs = allDrugs.stream()
                    .sorted(Comparator.comparing(Drugs::getStock))
                    .collect(Collectors.toList());
        }
        int offset = (page - 1) * pageSize;
        int endIndex = Math.min(offset + pageSize, filteredDrugs.size());
        return filteredDrugs.subList(offset, endIndex);
    }
    public static List<Drugs> getBadDrugsInPage(int page) {
        String sql = "SELECT * FROM get_bad_drugs_with_illness()";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setName(rs.getString("drug_name"));
            drug.setDosageForm(rs.getString("dosage_form"));
            drug.setStock(rs.getInt("stock"));
            drug.setAverage_stock(rs.getInt("average_stock"));
            System.out.println("the info:" + drug);
            return drug;
        });
    }
}
