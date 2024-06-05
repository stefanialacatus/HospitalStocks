package com.example.demo.DrugStock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/drugstock")
public class DrugStockController {

    @PostMapping("/drugsInPage")
    public ResponseEntity<Map<String, Map<String, Map<String, Object>>>> handlePostRequest(@RequestBody Integer page) {
        if (page == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Map<String, Map<String, Object>> result = DrugStockDAO.drugsInPage(page);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Map<String, Map<String, Map<String, Object>>> response = new HashMap<>();
        response.put("drugs", result);

        return ResponseEntity.ok(response);
    }
}
