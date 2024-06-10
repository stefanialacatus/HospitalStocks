package com.example.demo.Drugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/drugs")
public class DrugController {

    @Autowired
    private DrugDAO drugService;

    @GetMapping("/getAllDrugs")
    public List<Drugs> getAllDrugs() {
        return DrugDAO.getAllDrugs();
    }

    @GetMapping("/{id}")
    public Drugs getDrugById(@PathVariable("id") int id) {
        return DrugDAO.findById(id);
    }

    @GetMapping("/findByName")
    public List<Drugs> searchDrugsByName(@RequestParam("name") String name) {
        return DrugDAO.findByName(name);
    }

    @GetMapping("/getDrugsInPage")
    public List<Drugs> getDrugsInPage(@RequestParam("page") int page) {
        return DrugDAO.getAllDrugsFiltered(page);
    }
    @GetMapping("/getBadDrugsInPage")
    public List<Drugs> getBadDrugsInPage(@RequestParam("page") int page) {
        page = 1;
        return DrugDAO.getBadDrugsInPage(page);
    }
    @PostMapping("/filter")
    public void setFilter(@RequestBody Integer selectedValue) {
        DrugDAO.setFilter(selectedValue);
    }
    @GetMapping("/count")
    public int countDrugs() {
        return DrugDAO.getMedicinesAvailable();
    }
}