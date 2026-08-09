package com.chronosphere.app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Frontend se access allow karne ke liye
public class ChronoController {

    // Apni OpenWeather API key yahan daalein
    private final String WEATHER_API_KEY = "b91ed340d9e7869a4d0859f4ef892483"; 

    @GetMapping("/data")
    public Map<String, Object> getChronoData(@RequestParam double lat, @RequestParam double lon) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = new HashMap<>();

        // 1. Weather Data Fetch Karna
        try {
            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + WEATHER_API_KEY + "&units=metric";
            Object weatherData = restTemplate.getForObject(weatherUrl, Object.class);
            response.put("weather", weatherData);
        } catch (Exception e) {
            response.put("weatherError", "Atmos data unavailable");
        }

        // 2. Time Data Fetch Karna
        try {
            String timeUrl = "https://timeapi.io/api/Time/current/coordinate?latitude=" + lat + "&longitude=" + lon;
            Object timeData = restTemplate.getForObject(timeUrl, Object.class);
            response.put("time", timeData);
        } catch (Exception e) {
            response.put("timeError", "Temporal data unavailable (Oceanic region)");
        }

        return response; // Automatically JSON mein convert ho jayega
    }
}