package com.example.demo.model.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RegistroDTO {
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String dni;
    private LocalDate fechaNacimiento; // <-- camelCase

    // Si usas Lombok (@Data), los getters/setters se generan automáticamente
}
