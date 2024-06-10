package com.example.demo.Patient;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PatientSyncService {

    @Autowired
    @Getter
    private PatientDAO patientDAO;

    @Getter
    private List<Patient> cachedPatients;
    private Thread syncThread;
    private boolean running = true;

    @PostConstruct
    public void startSyncThread() {
        syncThread = new Thread(() -> {
            while (running) {
                synchronizePatientData();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        syncThread.start();
    }

    @PreDestroy
    public void stopSyncThread() {
        running = false;
        if (syncThread != null) {
            syncThread.interrupt();
        }
    }

    private void synchronizePatientData() {
        cachedPatients = patientDAO.getAllIllnesses();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println("Patient data synchronized at " + currentTime.format(formatter));
    }

}
