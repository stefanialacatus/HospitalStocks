package com.example.demo.Drugs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugService {

    @Autowired
    private DrugDAO drugDAO;

    public List<Drugs> getAllDrugs() {
        return DrugDAO.getAllDrugs();
    }

    public Drugs getDrugById(int id) {
        return DrugDAO.findById(id);
    }

    public List<Drugs> searchDrugsByName(String name) {
        return DrugDAO.findByName(name);
    }
}
