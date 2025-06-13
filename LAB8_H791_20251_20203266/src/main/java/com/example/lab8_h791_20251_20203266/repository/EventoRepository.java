package com.example.lab8_h791_20251_20203266.repository;


import com.example.lab8_h791_20251_20203266.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    public boolean existsByCiudadAndFechaEvento(String ciudad, LocalDate fechaEvento);
}