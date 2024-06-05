package com.example.demo.Illness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/illnesses")
public class IllnessController {

    @GetMapping("/getAllIllnesses")
    public List<Illness> getAllIllnesses() {
        return IllnessDAO.getAllIllnesses();
    }

    @GetMapping("/{id}")
    public Illness getIllnessById(@PathVariable("id") int id) {
        return IllnessDAO.findById(id);
    }

    @GetMapping("/findByName")
    public List<Illness> searchIllnessesByName(@RequestParam("name") String name) {
        return IllnessDAO.findByName(name);
    }
}
