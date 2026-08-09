package com.chronosphere.app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChronoController {

    private final String WEATHER_API_KEY = "b91ed340d9e7869a4d0859f4ef892483"; 

    @GetMapping("/data")
    public Map<String, Object> getChronoData(@RequestParam double lat, @RequestParam double lon) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Weather Data Fetch
            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + WEATHER_API_KEY + "&units=metric";
            Map<String, Object> weatherData = restTemplate.getForObject(weatherUrl, Map.class);
            response.put("weather", weatherData);

            // 2. Accurate Time Calculation using OpenWeather's timezone offset (in seconds)
            if (weatherData != null && weatherData.containsKey("timezone")) {
                int timezoneOffsetSeconds = (Integer) weatherData.get("timezone");
                
                // Calculate local time for those specific coordinates
                ZoneOffset offset = ZoneOffset.ofTotalSeconds(timezoneOffsetSeconds);
                ZonedDateTime localTime = ZonedDateTime.now(offset);
                
                Map<String, Object> timeData = new HashMap<>();
                // Format matching frontend expectation: ISO string
                timeData.put("dateTime", localTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                response.put("time", timeData);
            }

        } catch (Exception e) {
            response.put("error", "Data unavailable for this coordinate: " + e.getMessage());
        }

        return response;
    }
}