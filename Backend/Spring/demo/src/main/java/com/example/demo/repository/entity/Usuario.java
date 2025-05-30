package com.example.demo.repository.entity;

import lombok.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Usuario {

    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private LocalDate fechaNacimiento;
    private String imagen;
    private String password;

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Muchos usuarios pueden tener el mismo rol
    @JoinColumn(name = "id_rol") // Esta columna estará en la tabla usuarios
    @JsonBackReference // Evita la serialización recursiva
    private Rol rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Post> posts = new HashSet<>();


}
