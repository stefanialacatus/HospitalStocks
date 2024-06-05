package com.example.demo.DrugStock;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.HashMap;

@Repository
public class DrugStockDAO {

    @Autowired
    private static JdbcTemplate jdbcTemplate;

    public static Map<String, Map<String, Object>> drugsInPage(int pageNum) {
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

                Map<String, Object> drugInfo = new HashMap<>();
                drugInfo.put("DosageForm", dosageForm);
                drugInfo.put("Illness", illness);
                drugInfo.put("Quantity", quantity);

                drugsMap.put(drugName, drugInfo);
            }
            rs.close();
            stmt.close();
            return null;
        });
        return drugsMap;
    }
}