package com.eventlottery.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "tourist-service")
public interface TouristServiceClient {
    
    @GetMapping("/api/tourist/events")
    Map<String, Object> getAllEvents();
    
    @GetMapping("/api/tourist/events/{id}")
    Map<String, Object> getEventById(@PathVariable("id") int id);
    
    @GetMapping("/api/tourist/events/random")
    Map<String, Object> getRandomEvent();
    
    @PostMapping("/api/tourist/events")
    Map<String, Object> createEvent(@RequestBody Map<String, Object> event);
    
    @PostMapping("/api/tourist/events/nearby")
    Map<String, Object> getNearbyEvents(@RequestBody Map<String, Object> request);
} 