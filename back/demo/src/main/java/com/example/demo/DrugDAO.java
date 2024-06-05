package com.example.demo;

import java.sql.*;
import java.util.List;

public interface DrugDAO {
    // Select All Drugs
    public static List<Drugs> selectAllDrugs() throws SQLException {
        Connection con = AppConfig.getConnection();
        try (Statement stmt = con.createStatement();
             List<Drugs> rs = (List<Drugs>) stmt.executeQuery(
                     "select * from drugs")) {
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
