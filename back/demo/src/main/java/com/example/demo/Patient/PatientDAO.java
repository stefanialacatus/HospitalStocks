package com.example.demo.Patient;

import com.example.demo.Drugs.Drugs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
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

    public static List<Patient> getPatientsInPage(int page) {
        // Call the stored procedure or function to get drugs for the given page number
        String sql = "SELECT * FROM get_patient_illness_info(?)";
        return jdbcTemplate.query(sql, new Object[]{page}, (rs, rowNum) -> {
            Patient patient = new Patient();
            patient.setFirstName(rs.getString("first_name"));
            patient.setLastName(rs.getString("last_name"));
            patient.setIllnessName(rs.getString("illness_name"));

            System.out.println("the info:" + patient);
            return patient;
        });
    }
    public void addPatient(String firstName, String lastName, String illnessName) {
        System.out.println("ajuns la DAO");
        String sql = "SELECT add_patient(?, ?, ?)";
        try {
            jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, illnessName);
                ps.execute();
                return null;
            });
        } catch (Exception e) {
            // Handle the exception appropriately, e.g., log it or throw a custom exception
            e.printStackTrace();
        }
    }
    public void deletePatient(String firstName, String lastName) {
        String sql = "SELECT delete_patient(?, ?)";
        jdbcTemplate.execute(sql, (PreparedStatementCallback<Void>) ps -> {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.execute();
            return null;
        });
    }

}
