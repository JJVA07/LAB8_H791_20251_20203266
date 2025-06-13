package com.example.lab8_h791_20251_20203266.controller;

import com.example.lab8_h791_20251_20203266.dto.ClimaDTO;
import com.example.lab8_h791_20251_20203266.dto.EventoDTO;
import com.example.lab8_h791_20251_20203266.entity.Evento;
import com.example.lab8_h791_20251_20203266.repository.EventoRepository;
import com.example.lab8_h791_20251_20203266.service.WeatherService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EventoController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private EventoRepository eventoRepository;

    // 1. Consulta de eventos deportivos
    @GetMapping("/eventos")
    public List<EventoDTO> getEventos(@RequestParam String ciudad) {
        try {
            JsonNode root = weatherService.obtenerEventosDeportivos(ciudad);
            List<EventoDTO> lista = new ArrayList<>();

            LocalDate hoy = LocalDate.now();
            LocalDate maxFecha = hoy.plusDays(7);

            if (root.has("sports")) {
                for (JsonNode n : root.get("sports")) {
                    LocalDate fecha = LocalDate.parse(n.get("match_date").asText());
                    if (!fecha.isBefore(hoy) && !fecha.isAfter(maxFecha)) {
                        lista.add(new EventoDTO(
                                n.path("match").asText(),
                                n.path("match_date").asText(),
                                ciudad
                        ));
                    }
                }
            }
            return lista;

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener eventos deportivos para la ciudad: " + ciudad
            );
        }
    }

    // 2. Clima para el día del evento
    @GetMapping("/clima")
    public ClimaDTO getClima(@RequestParam String ciudad,
                             @RequestParam String fecha) {
        JsonNode days = weatherService.obtenerPronostico(ciudad, 7)
                .path("forecast").path("forecastday");

        for (JsonNode d : days) {
            if (d.path("date").asText().equals(fecha)) {
                JsonNode day = d.path("day");
                return new ClimaDTO(
                        day.path("condition").path("text").asText(),
                        day.path("maxtemp_c").asDouble(),
                        day.path("mintemp_c").asDouble()
                );
            }
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontró pronóstico para la fecha: " + fecha
        );
    }

    // 3. Registrar evento con clima
    @PostMapping("/evento")
    public ResponseEntity<Evento> registrarEvento(@RequestBody Evento evento) {
        // Verificar si ya existe un evento para la misma ciudad y fecha
        if (eventoRepository.existsByCiudadAndFechaEvento(evento.getCiudad(), evento.getFechaEvento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Evento ya registrado para esa ciudad y fecha");
        }

        Evento guardado = eventoRepository.save(evento);
        return ResponseEntity.ok(guardado);
    }


    // 4. Obtener todos los eventos
    @GetMapping("/evento")
    public List<Evento> obtenerEventos() {
        return eventoRepository.findAll();
    }

    @GetMapping("/evento/{id}")
    public ResponseEntity<Evento> obtenerEvento(@PathVariable Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));
        return ResponseEntity.ok(evento);
    }

}
