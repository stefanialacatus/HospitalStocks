package com.example.demo.Drugs;

import com.example.demo.Drugs.repository.DrugRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping({"/drugs"})
public class DrugController {
    @Autowired
    private DrugDAO drugService;
    @Autowired
    private DrugRepository drugRepository;

    public DrugController() {
    }

    @GetMapping({"/getAllDrugs"})
    public List<Drugs> getAllDrugs() {
        return DrugDAO.getAllDrugs();
    }

    @GetMapping({"/{id}"})
    public Drugs getDrugById(@PathVariable("id") int id) {
        return DrugDAO.findById(id);
    }

    @GetMapping({"/findByName"})
    public List<Drugs> searchDrugsByName(@RequestParam("name") String name) {
        return DrugDAO.findByName(name);
    }

    @GetMapping({"/getDrugsInPage"})
    public List<Drugs> getDrugsInPage(@RequestParam("page") int page) {
        return DrugDAO.getAllDrugsFiltered(page);
    }

    @GetMapping({"/getBadDrugsInPage"})
    public List<Drugs> getBadDrugsInPage(@RequestParam("page") int page) {
        page = 1;
        return DrugDAO.getBadDrugsInPage(page);
    }

    @PostMapping({"/filter"})
    public void setFilter(@RequestBody Integer selectedValue) {
        DrugDAO.setFilter(selectedValue);
    }

    @GetMapping({"/count"})
    public int countDrugs() {
        return DrugDAO.getMedicinesAvailable();
    }

    @GetMapping({"/search"})
    public List<DrugsJPA> search(@RequestParam(value = "name",defaultValue = "") String name) {
        if (name.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<DrugsJPA> suggestions = this.drugRepository.findByNameContainingIgnoreCase(name);
            return (List)suggestions.stream().limit(10L).collect(Collectors.toList());
        }
    }
}
