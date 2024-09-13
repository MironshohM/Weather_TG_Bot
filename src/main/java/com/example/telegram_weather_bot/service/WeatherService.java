package com.example.telegram_weather_bot.service;

import com.example.telegram_weather_bot.dto.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getWeatherByCity(String cityName) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", cityName)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric") // For Celsius
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();

        try {
            String response = restTemplate.getForObject(url, String.class);
            return formatWeatherResponse(response);
        } catch (HttpClientErrorException e) {
            // If the API returns a 404 error or similar, handle it here
            if (e.getStatusCode().value() == 404) {
                return "City not found. Please try again with a valid city name.";
            } else {
                return "Error retrieving weather data. Please try again later.";
            }
        }
    }

    private String formatWeatherResponse(String jsonResponse) {
        try {
            WeatherResponse weatherResponse = objectMapper.readValue(jsonResponse, WeatherResponse.class);

            String weatherMain = weatherResponse.getWeather()[0].getMain();
            String weatherDescription = weatherResponse.getWeather()[0].getDescription();
            double temp = weatherResponse.getMain().getTemp();
            double tempMin = weatherResponse.getMain().getTemp_min();
            double tempMax = weatherResponse.getMain().getTemp_max();
            int pressure = weatherResponse.getMain().getPressure();
            int humidity = weatherResponse.getMain().getHumidity();

            return String.format("Weather: %s (%s)\nTemperature: %.2f°C\nMin Temperature: %.2f°C\nMax Temperature: %.2f°C\nPressure: %d hPa\nHumidity: %d%%",
                    weatherMain, weatherDescription, temp, tempMin, tempMax, pressure, humidity);
        } catch (Exception e) {
            return "Error parsing weather data.";
        }
    }
}
