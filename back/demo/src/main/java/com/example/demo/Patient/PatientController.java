package com.example.demo.Patient;

import com.example.demo.DrugEntry.DrugEntry;
import com.example.demo.DrugEntry.DrugEntryRequest;
import com.example.demo.Patient.Patient;
import com.example.demo.Patient.PatientDAO;
import com.example.demo.Patient.PatientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientDAO patientsDAO;


    @Autowired
    private PatientDAO patientDAO;
    private PatientSyncService patientDataSyncService;
    @GetMapping("/findByName")
    public List<Patient> findPatientsByName(@RequestParam("name") String name) {
        List<Patient> patients = patientDAO.findByName(name);
        if (patients == null || patients.isEmpty()) {
            return Collections.emptyList();
        }
        return patients;
    }
    @PostMapping("/filter")
    public List<Patient> filterPatients(@RequestBody String filter) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String> filterMap = mapper.readValue(filter, Map.class);
            String selectedValue = filterMap.get("selectedValue");
            List<Patient> patients = patientDAO.filterPatients(selectedValue);
            System.out.println("Filtering patients");
            return patients;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    @GetMapping("/getPatientsInPage")
    public List<Patient> getPatientsInPage(@RequestParam("page") int page) {
        List<Patient> patients = patientDAO.getPatientsInPage(page);
        if (patients == null || patients.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if no patients found
        }
        return patients;
    }

   /* @PostMapping("/addPatient")
    public ResponseEntity<String> addPatient(@RequestBody PatientRequest request) {
        try {
            patientDataSyncService.getPatientDAO().addPatient(request.getFirstName(), request.getLastName(), request.getIllnessName());
            return new ResponseEntity<>("Patient added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding patient: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletePatient/{firstName}/{lastName}")
    public ResponseEntity<String> deletePatient(@PathVariable String firstName, @PathVariable String lastName) {
        patientDataSyncService.getPatientDAO().deletePatient(firstName, lastName);
        return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
    }*/
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
