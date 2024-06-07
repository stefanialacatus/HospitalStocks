package com.example.demo.DrugEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping
public class DrugEntryController {

    private static final Logger log = LoggerFactory.getLogger(DrugEntryController.class);

    private final DrugEntryDAO drugEntryDAO;

    @Autowired
    public DrugEntryController(DrugEntryDAO drugEntryDAO) {
        this.drugEntryDAO = drugEntryDAO;
    }

    @PostMapping("/addEntry")
    public ResponseEntity<String> addDrugEntry(@RequestBody DrugEntryRequest request) {
        DrugEntry drugEntry = new DrugEntry();
        drugEntry.setQuantity(request.getQuantity());
        System.out.println("DrugEntry: " + drugEntry);

        try {
            drugEntryDAO.addDrugEntry(drugEntry, request.getDrugName(), request.getSupplierName());
            System.out.println("Entry added successfully");
            return ResponseEntity.ok("Entry added successfully");
        } catch (IllegalArgumentException e) {
            log.error("Error adding entry: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (SQLException e) {
            log.error("An unexpected SQL error occurred while adding entry", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the entry");
        } catch (Exception e) {
            log.error("An unexpected error occurred while adding entry", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the entry");
        }
    }
}
