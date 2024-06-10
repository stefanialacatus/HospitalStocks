package com.example.demo.Drugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DrugDAO {

    private static JdbcTemplate jdbcTemplate = null;
    @Autowired
    private DrugsRepository drugsRepository;

    public List<Drugs> getAllDrugs() {
        return drugsRepository.findAll();
    }

    public Drugs findById(int id) {
        return drugsRepository.findById(id).orElse(null);
    }

    public List<Drugs> findByName(String name) {
        return drugsRepository.findByNameContainingIgnoreCase(name);
    }
    public int getCount() {
        return drugsRepository.getCount();
    }

<<<<<<< Updated upstream
    public static List<Drugs> findByName(String name) {
        String query = "SELECT d.name AS drug_name, " +
                "d.dosage_form, " +
                "(SELECT i.name FROM illnesses i JOIN illness_drug id ON i.id = id.illness_id WHERE id.drug_id = d.id LIMIT 1) AS illness, " +
                "d.stock " +
                "FROM drugs d " +
                "WHERE d.name LIKE ?";
        return jdbcTemplate.query(query, new Object[]{"%" + name + "%"}, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setName(rs.getString("drug_name"));
            drug.setDosageForm(rs.getString("dosage_form"));
            drug.setIllness(rs.getString("illness"));
            drug.setStock(rs.getInt("stock"));
            System.out.println("Found drug:" + drug);
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
    public static List<Drugs> getDrugsInPage(int page) {
        // Call the stored procedure or function to get drugs for the given page number
        String sql = "SELECT * FROM get_drugs_info_in_page_table(?)";
        return jdbcTemplate.query(sql, new Object[]{page}, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setName(rs.getString("drug_name"));
            drug.setDosageForm(rs.getString("dosage_form"));
            drug.setIllness(rs.getString("illness"));
            drug.setStock(rs.getInt("stock"));
            //System.out.println("the info:" + drug);
            return drug;
        });
=======
    public int getMedicinesAvailable() {
        return drugsRepository.getMedicinesAvailable();
    }

    /*de aici trb modif pr hibernate*/
    public List<Map<String, Object>> getDrugsInPage(int page) {
        int pageSize = 8;
        Pageable pageable = (Pageable) PageRequest.of(page - 1, pageSize);
        List<Object[]> results = drugsRepository.findAllDrugSummaries(pageable);
        return convertToMap(results);
>>>>>>> Stashed changes
    }

    public static List<Drugs> getBadDrugsInPage(int page) {
        String sql = "SELECT * FROM get_bad_drugs_with_illness()";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Drugs drug = new Drugs();
            drug.setName(rs.getString("drug_name"));
            drug.setDosageForm(rs.getString("dosage_form"));
            drug.setIllness(rs.getString("illness"));
            drug.setStock(rs.getInt("stock"));
            //System.out.println("the info:" + drug);
            return drug;
        });


    }
    private List<Map<String, Object>> convertToMap(List<Object[]> results) {
        List<Map<String, Object>> mappedResults = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", row[0]);
            map.put("dosageForm", row[1]);
            map.put("illness", row[2]);
            map.put("stock", row[3]);
            mappedResults.add(map);
        }
        return mappedResults;
    }
}
