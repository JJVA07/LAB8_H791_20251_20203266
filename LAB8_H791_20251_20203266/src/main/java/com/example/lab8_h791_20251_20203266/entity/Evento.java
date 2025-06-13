package com.example.lab8_h791_20251_20203266.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "evento")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String ciudad;
    private LocalDate fechaEvento;
    private String condicionClimatica;
    private Double temperaturaMax;
    private Double temperaturaMin;
}