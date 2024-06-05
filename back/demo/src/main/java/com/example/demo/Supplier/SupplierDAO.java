package com.example.demo.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SupplierDAO {
    private static JdbcTemplate jdbcTemplate = null;

    @Autowired
    public SupplierDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public static int getCount() {
        String sql = "SELECT COUNT(*) FROM Supplier";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}