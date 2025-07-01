package com.eventlottery.app;

import com.eventlottery.app.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/lottery")
public class LotteryController {
    
    @Autowired
    private LotteryService lotteryService;
    
    @PostMapping("/draw")
    public Map<String, Object> lottery(@RequestBody Map<String, Object> request) {
        return lotteryService.performLottery(request);
    }
    
    @PostMapping("/lottery")
    public Map<String, Object> lotteryUi(@RequestBody Map<String, Object> request) {
        return lotteryService.performLottery(request);
    }
    
    @GetMapping("/events")
    public Map<String, Object> getAllEvents() {
        return lotteryService.getAllEvents();
    }
    
    @GetMapping("/events/random")
    public Map<String, Object> getRandomEvent() {
        return lotteryService.getRandomEvent();
    }
    
    @PostMapping("/events")
    public Map<String, Object> createEvent(@RequestBody Map<String, Object> eventRequest) {
        return lotteryService.createEvent(eventRequest);
    }
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Event Lottery Application");
        health.put("timestamp", new Date());
        return health;
    }
} 