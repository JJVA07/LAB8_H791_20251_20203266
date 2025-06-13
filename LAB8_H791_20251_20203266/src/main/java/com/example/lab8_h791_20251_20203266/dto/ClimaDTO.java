package com.example.lab8_h791_20251_20203266.dto;

public record ClimaDTO(
        String condition, // condition.text
        double maxTempC,  // maxtemp_c
        double minTempC   // mintemp_c
) {}