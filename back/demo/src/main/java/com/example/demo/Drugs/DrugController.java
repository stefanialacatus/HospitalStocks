package com.example.demo.Drugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drugs")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @GetMapping("/getAllDrugs")
    public List<Drugs> getAllDrugs() {
        return drugService.getAllDrugs();
    }

    @GetMapping("/{id}")
    public Drugs getDrugById(@PathVariable("id") int id) {
        return drugService.getDrugById(id);
    }

    @GetMapping("/findByName")
    public List<Drugs> searchDrugsByName(@RequestParam("name") String name) {
        return drugService.searchDrugsByName(name);
    }
}
