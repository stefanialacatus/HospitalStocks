package com.example.demo.Drugs;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface DrugsRepository extends JpaRepository<Drugs, Integer> {
    List<Drugs> findByNameContainingIgnoreCase(String name);
    @Query("SELECT COUNT(d) FROM Drugs d")
    int getCount();

    @Query("SELECT COUNT(d) FROM Drugs d WHERE d.stock > 0")
    int getMedicinesAvailable();

    @Query("SELECT d.name, d.dosageForm, i.name, d.stock " +
            "FROM Drugs d " +
            "JOIN IllnessDrug id ON d.id = id.drugId " +
            "JOIN Illnesses i ON id.illnessId = i.id")
    List<Object[]> findAllDrugSummaries(Pageable pageable);


    @Query("SELECT d.name, d.dosageForm, d.illness, d.stock FROM Drugs d WHERE d.name LIKE %:name%")
    List<Object[]> findDrugSummariesByNameContainingIgnoreCase(String name, Pageable pageable);
}
