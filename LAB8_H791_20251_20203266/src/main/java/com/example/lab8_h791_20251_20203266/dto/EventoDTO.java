package com.example.lab8_h791_20251_20203266.dto;

public record EventoDTO(
        String match,    // nombre del partido o liga
        String date,     // fecha de inicio
        String location  // ciudad o liga para mostrar ubicación
) {}