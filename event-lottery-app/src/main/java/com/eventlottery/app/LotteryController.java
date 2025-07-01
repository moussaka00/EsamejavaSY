package com.eventlottery.app;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class LotteryController {
    @PostMapping("/lottery")
    public Map<String, Object> lottery(@RequestBody Map<String, Object> request) {
        // Mock: genera una lista di eventi finti
        List<Map<String, Object>> allEvents = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> event = new HashMap<>();
            event.put("title", "Evento " + i);
            event.put("description", "Descrizione evento " + i);
            event.put("location", "Luogo " + i);
            event.put("dateFrom", LocalDateTime.now().plusDays(i).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            event.put("dateTo", LocalDateTime.now().plusDays(i).plusHours(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            allEvents.add(event);
        }
        // Scegli un evento casuale
        Map<String, Object> selectedEvent = allEvents.get(ThreadLocalRandom.current().nextInt(allEvents.size()));
        Map<String, Object> response = new HashMap<>();
        response.put("totalEvents", allEvents.size());
        response.put("selectedEvent", selectedEvent);
        response.put("allEvents", allEvents);
        return response;
    }
} 