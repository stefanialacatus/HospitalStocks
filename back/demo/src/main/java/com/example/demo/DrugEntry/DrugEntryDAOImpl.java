package com.example.demo.DrugEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DrugEntryDAOImpl implements DrugEntryDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DrugEntryDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addDrugEntry(DrugEntry drugEntry) {
        String sql = "INSERT INTO drugentries (drugid, supplierid, batchnumber, quantity, entrydate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                drugEntry.getDrugId(),
                drugEntry.getSupplierId(),
                drugEntry.getBatchNumber(),
                drugEntry.getQuantity(),
                drugEntry.getEntryDate());
    }
}
