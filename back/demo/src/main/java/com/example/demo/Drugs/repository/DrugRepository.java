package com.example.demo.Drugs.repository;

import com.example.demo.Drugs.DrugsJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugRepository extends JpaRepository<DrugsJPA, Integer> {
    List<DrugsJPA> findByNameContainingIgnoreCase(String name);
}
