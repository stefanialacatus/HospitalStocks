package com.example.demo.Drugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/drugs")
public class DrugController {

    @Autowired
    private DrugDAO drugService;
    @Autowired
    List<Drugs> drugList=new ArrayList<>();

    @GetMapping("/getAllDrugs")
    public List<Drugs> getAllDrugs() {
        return drugService.getAllDrugs();
    }

    @GetMapping("/{id}")
    public Drugs getDrugById(@PathVariable("id") int id) {
        return drugService.findById(id);
    }

    @GetMapping("/findByName")
    public List<Drugs> searchDrugsByName(@RequestParam("name") String name) {
<<<<<<< Updated upstream

        return DrugDAO.findByName(name);
=======
        return drugService.findByName(name);
>>>>>>> Stashed changes
    }

    @GetMapping("/getDrugsInPage")
    public List<Map<String, Object>> getDrugsInPage(@RequestParam("page") int page) {
        return drugService.getDrugsInPage(page);
    }
    @GetMapping("/getBadDrugsInPage")
    public List<Drugs> getBadDrugsInPage(@RequestParam("page") int page) {
        return DrugDAO.getBadDrugsInPage(page);
    }

    @GetMapping("/search")
    public List<Drugs> search(@RequestParam(value = "name", defaultValue = "placeholder_test") String name) {
        return drugService.findByName(name);
    }

    @GetMapping("/searchSuggestions")
    public List<String> searchSuggestions(@RequestParam(value = "query") String query) {
        List<Drugs> drugs = drugService.findByName(query);
        List<String> suggestions = new ArrayList<>();
        for (Drugs drug : drugs) {
            suggestions.add(drug.getName());
        }
        return suggestions;
    }
}

