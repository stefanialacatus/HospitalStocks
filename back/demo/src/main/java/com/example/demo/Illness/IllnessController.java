package com.example.demo.Illness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/illnesses")
public class IllnessController {

    @Autowired
    private IllnessService illnessService;

    @GetMapping("/getAllIllnesses")
    public List<Illness> getAllIllnesses() {
        return illnessService.getAllIllnesses();
    }

    @GetMapping("/{id}")
    public Illness getIllnessById(@PathVariable("id") int id) {
        return illnessService.getIllnessById(id);
    }

    @GetMapping("/findByName")
    public List<Illness> searchIllnessesByName(@RequestParam("name") String name) {
        return illnessService.searchIllnessesByName(name);
    }
}
