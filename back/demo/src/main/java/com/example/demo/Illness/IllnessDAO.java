package com.example.demo.Illness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IllnessDAO {

    private static JdbcTemplate jdbcTemplate = null;

    @Autowired
    public IllnessDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static List<Illness> getAllIllnesses() {
        String query = "SELECT * FROM Illnesses";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Illness illness = new Illness();
            illness.setId(rs.getInt("id"));
            illness.setName(rs.getString("name"));
            return illness;
        });
    }

    public static Illness findById(int id) {
        String query = "SELECT * FROM Illnesses WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (rs, rowNum) -> {
            Illness illness = new Illness();
            illness.setId(rs.getInt("id"));
            illness.setName(rs.getString("name"));
            return illness;
        });
    }

    public static List<Illness> findByName(String name) {
        String query = "SELECT * FROM Illnesses WHERE name LIKE ?";
        return jdbcTemplate.query(query, new Object[]{"%" + name + "%"}, (rs, rowNum) -> {
            Illness illness = new Illness();
            illness.setId(rs.getInt("id"));
            illness.setName(rs.getString("name"));
            return illness;
        });
    }
    public static int getCount() {
        String sql = "SELECT COUNT(*) FROM Illnesses";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}