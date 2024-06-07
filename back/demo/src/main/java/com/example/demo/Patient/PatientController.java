package com.example.demo.Drugs;

import com.example.demo.DrugEntry.DrugEntry;
import com.example.demo.DrugEntry.DrugEntryRequest;
import com.example.demo.Patient.Patient;
import com.example.demo.Patient.PatientDAO;
import com.example.demo.Patient.PatientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientDAO patientsDAO;


    @GetMapping("/getPatientsInPage")
    public List<Patient> getPatientsInPage(@RequestParam("page") int page) {
        // Call the method to get drugs for the given page number
        return PatientDAO.getPatientsInPage(page);
    }
    @PostMapping("/addPatient")
    public ResponseEntity<String> addPatient(@RequestBody PatientRequest request) {
        try {
            System.out.println("ajuns la controller");
            patientsDAO.addPatient(request.getFirstName(), request.getLastName(), request.getIllnessName());
            return new ResponseEntity<>("Patient added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/deletePatient/{firstName}/{lastName}")
    public ResponseEntity<String> deletePatient(@PathVariable String firstName, @PathVariable String lastName) {
        patientsDAO.deletePatient(firstName, lastName);
        return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
    }

}
