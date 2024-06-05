package com.example.demo.DrugEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DrugEntryController {

    private final DrugEntryService drugEntryService;

    @Autowired
    public DrugEntryController(DrugEntryService drugEntryService) {
        this.drugEntryService = drugEntryService;
    }

    @PostMapping("/drugentries/add")
    public void addDrugEntry(@RequestBody DrugEntry drugEntry, String drugName, String supplierName) {
        drugEntryService.addDrugEntry(drugEntry, drugName, supplierName);
    }
}
