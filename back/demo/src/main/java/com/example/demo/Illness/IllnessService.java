package com.example.demo.Illness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IllnessService {

    @Autowired
    private IllnessDAO illnessDAO;

    public List<Illness> getAllIllnesses() {
        return illnessDAO.selectAllIllnesses();
    }

    public Illness getIllnessById(int id) {
        return illnessDAO.findById(id);
    }

    public List<Illness> searchIllnessesByName(String name) {
        return illnessDAO.findByName(name);
    }
}
