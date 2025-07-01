package com.eventlottery.app.service;

import com.eventlottery.app.client.TouristServiceClient;
import com.eventlottery.app.client.RandomServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LotteryService {
    
    @Autowired
    private TouristServiceClient touristServiceClient;
    
    @Autowired
    private RandomServiceClient randomServiceClient;
    
    public Map<String, Object> performLottery(Map<String, Object> request) {
        // Adatta i parametri per il tourist-service
        double lat = getDouble(request, "lat", getDouble(request, "latitude", 0));
        double lon = getDouble(request, "lon", getDouble(request, "longitude", 0));
        int radius = getInt(request, "radius", 10000);
        int maxEvents = getInt(request, "maxEvents", 10);
        Map<String, Object> tsReq = Map.of(
            "lat", lat,
            "lon", lon,
            "radius", radius
        );
        Map<String, Object> eventsResponse = touristServiceClient.getNearbyEvents(tsReq);
        @SuppressWarnings("unchecked")
        List<Object> events = (List<Object>) eventsResponse.get("events");
        if (events == null || events.isEmpty()) {
            return Map.of("totalEvents", 0, "allEvents", List.of());
        }
        // Limita il numero di eventi
        if (events.size() > maxEvents) {
            events = events.subList(0, maxEvents);
        }
        // Chiede un indice random
        Map<String, Object> randomReq = Map.of("max", events.size());
        Map<String, Object> randomResp = randomServiceClient.getRandomIndex(randomReq);
        if (randomResp.get("index") == null) {
            return Map.of("error", "Errore random-service", "randomResponse", randomResp);
        }
        int index = (int) (randomResp.get("index") instanceof Integer ? randomResp.get("index") : ((Number)randomResp.get("index")).intValue());
        Object selectedRaw = events.get(index);
        Map<String, Object> selectedEvent = mapOdhEvent(selectedRaw);
        List<Map<String, Object>> mappedEvents = new ArrayList<>();
        for (Object e : events) mappedEvents.add(mapOdhEvent(e));
        return Map.of(
            "selectedEvent", selectedEvent,
            "selectedIndex", index,
            "totalEvents", events.size(),
            "allEvents", mappedEvents
        );
    }
    
    private Map<String, Object> mapOdhEvent(Object raw) {
        if (!(raw instanceof Map)) return Map.of();
        Map<?, ?> e = (Map<?, ?>) raw;
        // Title
        String title = extractLangOrString(e.get("Title"), "it", "en");
        // Description
        String description = extractLangOrString(e.get("ShortDescription"), "it", "en");
        // Location
        String location = "";
        if (e.get("LocationInfo") instanceof Map) {
            Map<?, ?> loc = (Map<?, ?>) e.get("LocationInfo");
            location = extractLangOrString(loc.get("Name"), "it", "en");
        }
        // Date
        String dateFrom = e.getOrDefault("DateFrom", "").toString();
        String dateTo = e.getOrDefault("DateTo", "").toString();
        return Map.of(
            "title", title,
            "description", description,
            "location", location,
            "dateFrom", dateFrom,
            "dateTo", dateTo
        );
    }
    
    private String extractLangOrString(Object obj, String... langs) {
        if (obj == null) return "";
        if (obj instanceof String) return (String) obj;
        if (obj instanceof Map) return extractLang((Map<?, ?>) obj, langs);
        return obj.toString();
    }
    
    private String extractLang(Map<?, ?> map, String... langs) {
        if (map == null) return "";
        for (String lang : langs) {
            if (map.containsKey(lang)) return map.get(lang).toString();
        }
        // fallback
        if (!map.isEmpty()) return map.values().iterator().next().toString();
        return "";
    }
    
    private double getDouble(Map<String, Object> map, String key, double def) {
        Object v = map.get(key);
        if (v == null) return def;
        try { return Double.parseDouble(v.toString()); } catch (Exception e) { return def; }
    }
    
    private int getInt(Map<String, Object> map, String key, int def) {
        Object v = map.get(key);
        if (v == null) return def;
        try { return Integer.parseInt(v.toString()); } catch (Exception e) { return def; }
    }
    
    public Map<String, Object> getRandomEvent() {
        try {
            return touristServiceClient.getRandomEvent();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Errore nel recupero evento casuale: " + e.getMessage());
            return error;
        }
    }
    
    public Map<String, Object> getAllEvents() {
        try {
            return touristServiceClient.getAllEvents();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Errore nel recupero eventi: " + e.getMessage());
            return error;
        }
    }
    
    public Map<String, Object> createEvent(Map<String, Object> eventRequest) {
        try {
            return touristServiceClient.createEvent(eventRequest);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Errore nella creazione evento: " + e.getMessage());
            return error;
        }
    }
} 