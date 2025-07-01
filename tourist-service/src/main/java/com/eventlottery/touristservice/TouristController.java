package com.eventlottery.touristservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/tourist")
public class TouristController {
    
    private final List<Map<String, Object>> events = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String ODH_API = "https://tourism.opendatahub.com/v1/Event";
    
    public TouristController() {
        // Inizializza con alcuni eventi di esempio
        initializeEvents();
    }
    
    private void initializeEvents() {
        events.add(createEvent("Festa della Pizza", "Celebrazione della pizza napoletana", "Napoli", 1));
        events.add(createEvent("Carnevale di Venezia", "Maschere e tradizioni veneziane", "Venezia", 2));
        events.add(createEvent("Palio di Siena", "Corsa storica tra le contrade", "Siena", 3));
        events.add(createEvent("Festa dei Gigli", "Festa patronale di Nola", "Nola", 4));
        events.add(createEvent("Sagra del Tartufo", "Degustazione di tartufi bianchi", "Alba", 5));
        events.add(createEvent("Festa della Vendemmia", "Celebrazione del vino", "Chianti", 6));
        events.add(createEvent("Sagra del Pesce", "Pesce fresco del Mediterraneo", "Catania", 7));
        events.add(createEvent("Festa delle Luci", "Illuminazione artistica della città", "Milano", 8));
    }
    
    private Map<String, Object> createEvent(String title, String description, String location, int daysFromNow) {
        Map<String, Object> event = new HashMap<>();
        event.put("id", events.size() + 1);
        event.put("title", title);
        event.put("description", description);
        event.put("location", location);
        event.put("dateFrom", LocalDateTime.now().plusDays(daysFromNow).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        event.put("dateTo", LocalDateTime.now().plusDays(daysFromNow).plusHours(3).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        event.put("category", "Cultural");
        event.put("maxParticipants", 100 + new Random().nextInt(200));
        return event;
    }
    
    @GetMapping("/events")
    public Map<String, Object> getAllEvents() {
        Map<String, Object> response = new HashMap<>();
        response.put("events", events);
        response.put("totalEvents", events.size());
        response.put("timestamp", new Date());
        return response;
    }
    
    @GetMapping("/events/{id}")
    public Map<String, Object> getEventById(@PathVariable int id) {
        Optional<Map<String, Object>> event = events.stream()
                .filter(e -> (Integer) e.get("id") == id)
                .findFirst();
        
        Map<String, Object> response = new HashMap<>();
        if (event.isPresent()) {
            response.put("event", event.get());
            response.put("found", true);
        } else {
            response.put("error", "Evento non trovato");
            response.put("found", false);
        }
        return response;
    }
    
    @PostMapping("/events")
    public Map<String, Object> createEvent(@RequestBody Map<String, Object> eventRequest) {
        Map<String, Object> newEvent = new HashMap<>(eventRequest);
        newEvent.put("id", events.size() + 1);
        events.add(newEvent);
        
        Map<String, Object> response = new HashMap<>();
        response.put("event", newEvent);
        response.put("message", "Evento creato con successo");
        response.put("totalEvents", events.size());
        return response;
    }
    
    @GetMapping("/events/random")
    public Map<String, Object> getRandomEvent() {
        if (events.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Nessun evento disponibile");
            return error;
        }
        
        int randomIndex = new Random().nextInt(events.size());
        Map<String, Object> selectedEvent = events.get(randomIndex);
        
        Map<String, Object> response = new HashMap<>();
        response.put("selectedEvent", selectedEvent);
        response.put("selectedIndex", randomIndex);
        response.put("totalEvents", events.size());
        return response;
    }
    
    @PostMapping("/events/nearby")
    public Map<String, Object> getNearbyEvents(@RequestBody Map<String, Object> request) {
        double lat, lon;
        int radius = 10000; // default 10km
        try {
            lat = Double.parseDouble(request.get("lat").toString());
            lon = Double.parseDouble(request.get("lon").toString());
            if (request.containsKey("radius")) {
                radius = Integer.parseInt(request.get("radius").toString());
            }
        } catch (Exception e) {
            return Map.of("error", "Parametri non validi: " + e.getMessage());
        }
        // Costruisci la query per ODH
        String url = ODH_API + "?latitude=" + lat + "&longitude=" + lon + "&radius=" + radius + "&pagesize=20";
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> body = response.getBody();
            List<Object> events = (List<Object>) body.getOrDefault("Items", Collections.emptyList());
            return Map.of(
                "events", events,
                "totalEvents", events.size(),
                "source", "OpenDataHub Bolzano",
                "lat", lat,
                "lon", lon,
                "radius", radius
            );
        } catch (Exception e) {
            return Map.of("error", "Errore chiamando ODH: " + e.getMessage());
        }
    }
} 