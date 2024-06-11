package com.example.demo.Patient;

import com.example.demo.Drugs.Drugs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PatientDAO {

    private static JdbcTemplate jdbcTemplate = null;

    @Autowired
    public PatientDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Patient> getAllPatients() {
        String query = "SELECT p.first_name," +
                "       p.last_name," +
                "       i.name AS illness_name " +
                "FROM patients p " +
                "JOIN patients_illnesses pi ON p.id = pi.patient_id " +
                "JOIN illnesses i ON pi.illness_id = i.id";

        List<Patient> patients = jdbcTemplate.query(query, (rs, rowNum) -> {
            Patient patient = new Patient();
            patient.setFirstName(rs.getString("first_name"));
            patient.setLastName(rs.getString("last_name"));
            patient.setIllnessName(rs.getString("illness_name"));
            return patient;
        });
        return patients;
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

    public List<Patient> filterPatients(String filter) {
        System.out.println("filter received: " + filter);
        List<Patient> patients = getAllPatients();
        if ("az".equals(filter)) {
            return patients.stream()
                    .sorted(Comparator.comparing(Patient::getFirstName))
                    .collect(Collectors.toList());
        } else if ("za".equals(filter)) {
            return patients.stream()
                    .sorted(Comparator.comparing(Patient::getFirstName).reversed())
                    .collect(Collectors.toList());
        }
        else return patients;

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
        String sql = "SELECT * FROM patient_info_nopage()";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
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
            System.err.println("Error adding patient: " + e.getMessage());
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

    public List<Patient> searchByName(String name) {
        String query = "SELECT p.first_name," +
                "       p.last_name," +
                "       i.name AS illness_name " +
                "FROM patients p " +
                "JOIN patients_illnesses pi ON p.id = pi.patient_id " +
                "JOIN illnesses i ON pi.illness_id = i.id " +
                "WHERE LOWER(p.first_name) LIKE ? " +
                "   OR LOWER(p.last_name) LIKE ?;";
        return jdbcTemplate.query(query, new Object[]{"%" + name.toLowerCase() + "%", "%" + name.toLowerCase() + "%"}, (rs, rowNum) -> {
            Patient patient = new Patient();
            patient.setFirstName(rs.getString("first_name"));
            patient.setLastName(rs.getString("last_name"));
            patient.setIllnessName(rs.getString("illness_name"));
            return patient;
        });
    }


}
