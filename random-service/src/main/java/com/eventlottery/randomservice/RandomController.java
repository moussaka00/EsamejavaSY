package com.eventlottery.randomservice;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/random")
public class RandomController {
    
    @GetMapping("/number/{max}")
    public Map<String, Object> getRandomNumber(@PathVariable int max) {
        int randomNumber = new Random().nextInt(max) + 1;
        Map<String, Object> response = new HashMap<>();
        response.put("randomNumber", randomNumber);
        response.put("max", max);
        response.put("timestamp", new Date());
        return response;
    }
    
    @PostMapping("/select")
    public Map<String, Object> selectRandomFromList(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<Object> items = (List<Object>) request.get("items");
        
        if (items == null || items.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Lista vuota o non valida");
            return error;
        }
        
        int randomIndex = new Random().nextInt(items.size());
        Object selectedItem = items.get(randomIndex);
        
        Map<String, Object> response = new HashMap<>();
        response.put("selectedItem", selectedItem);
        response.put("selectedIndex", randomIndex);
        response.put("totalItems", items.size());
        response.put("timestamp", new Date());
        
        return response;
    }

    @PostMapping("/index")
    public Map<String, Object> getRandomIndex(@RequestBody Map<String, Object> request) {
        Object maxObj = request.get("max");
        int max;
        try {
            max = Integer.parseInt(String.valueOf(maxObj));
        } catch (Exception e) {
            return Map.of("error", "Parametro 'max' non valido");
        }
        if (max <= 0) {
            return Map.of("error", "Parametro 'max' deve essere maggiore di 0");
        }
        int index = new Random().nextInt(max);
        return Map.of("index", index);
    }
} 