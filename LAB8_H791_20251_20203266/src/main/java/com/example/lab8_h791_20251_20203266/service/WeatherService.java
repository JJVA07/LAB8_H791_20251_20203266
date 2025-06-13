package com.example.lab8_h791_20251_20203266.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.base}")
    private String apiBase;

    private final RestTemplate restTemplate = new RestTemplate();

    /** Devuelve todos los eventos deportivos próximos (hasta 7 días). */
    public JsonNode obtenerEventosDeportivos(String ciudad) {
        String url = String.format(
                "%s/sports.json?key=%s&q=%s&days=7",
                apiBase, apiKey, ciudad
        );
        return restTemplate.getForObject(url, JsonNode.class);
    }

    /** Devuelve el pronóstico climático de los próximos X días (usado 7). */
    public JsonNode obtenerPronostico(String ciudad, int dias) {
        String url = String.format(
                "%s/forecast.json?key=%s&q=%s&days=%d",
                apiBase, apiKey, ciudad, dias
        );
        return restTemplate.getForObject(url, JsonNode.class);
    }
}
