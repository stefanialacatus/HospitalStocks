package com.example.demo.DrugConsumption;

import com.example.demo.DrugEntry.DrugEntry;
import com.example.demo.DrugEntry.DrugEntryController;
import com.example.demo.DrugEntry.DrugEntryDAO;
import com.example.demo.DrugEntry.DrugEntryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class DrugConsumptionController {
    private final DrugConsumptionDAO drugConsumptionDAO;
    private static final Logger log = LoggerFactory.getLogger(DrugEntryController.class);
    @Autowired
    public DrugConsumptionController(DrugConsumptionDAO drugConsumptionDAO) {
        this.drugConsumptionDAO = drugConsumptionDAO;
    }
    @PostMapping("/consumeEntry")
    public ResponseEntity<String> consumeDrugEntry(@RequestBody DrugEntryRequest request) {
        DrugConsumption drugConsumption= new DrugConsumption();
        drugConsumption.setQuantity(request.getQuantity());
        System.out.println("DrugEntry: " + drugConsumption);

        try {
            drugConsumptionDAO.consumeDrugEntry(drugConsumption, request.getDrugName());
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
