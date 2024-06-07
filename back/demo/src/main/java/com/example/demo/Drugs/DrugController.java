package com.example.demo.Drugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
        // Call the method to get drugs for the given page number
        return DrugDAO.getDrugsInPage(page);
    }
}
