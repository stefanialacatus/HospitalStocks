package com.example.demo.Patient;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Data
public class PatientRequest {
    private String firstName;
    private String lastName;
    private String illnessName;
}
