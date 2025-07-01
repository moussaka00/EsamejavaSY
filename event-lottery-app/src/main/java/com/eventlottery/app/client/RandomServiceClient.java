package com.eventlottery.app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "random-service")
public interface RandomServiceClient {
    
    @GetMapping("/api/random/number/{max}")
    Map<String, Object> getRandomNumber(@PathVariable("max") int max);
    
    @PostMapping("/api/random/select")
    Map<String, Object> selectRandomFromList(@RequestBody Map<String, Object> request);

    @PostMapping("/api/random/index")
    Map<String, Object> getRandomIndex(@RequestBody Map<String, Object> request);
} 