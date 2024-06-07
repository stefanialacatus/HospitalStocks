package com.example.demo.Patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatientDAO {

    private static JdbcTemplate jdbcTemplate = null;

    @Autowired
    public PatientDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static List<Patient> getAllIllnesses() {
        String query = "SELECT * FROM Patients";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Patient illness = new Patient();
            illness.setId(rs.getInt("id"));
            illness.setFirstName(rs.getString("first_name"));
            illness.setLastName(rs.getString("last_name"));
            return illness;
        });
    }

    public static Patient findById(int id) {
        String query = "SELECT * FROM Patients WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (rs, rowNum) -> {
            Patient illness = new Patient();
            illness.setId(rs.getInt("id"));
            illness.setFirstName(rs.getString("first_name"));
            illness.setLastName(rs.getString("last_name"));
            return illness;
        });
    }

    public static List<Patient> findByName(String name) {
        String query = "SELECT * FROM Patients WHERE name LIKE ?";
        return jdbcTemplate.query(query, new Object[]{"%" + name + "%"}, (rs, rowNum) -> {
            Patient illness = new Patient();
            illness.setId(rs.getInt("id"));
            illness.setFirstName(rs.getString("first_name"));
            illness.setLastName(rs.getString("last_name"));
            return illness;
        });
    }
    public static int getCount() {
        String sql = "SELECT COUNT(*) FROM Patients";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public static String getMostUsedMedicine(){
        String sql = "SELECT name from drugs where id = (select get_most_consumed_drug(CURRENT_DATE))";
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
