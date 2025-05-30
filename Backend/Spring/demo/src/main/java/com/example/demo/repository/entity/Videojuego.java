package com.example.demo.repository.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "videojuegos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Videojuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private Integer duracion;
    private Integer jugadores;
    private LocalDate lanzamiento;
    private String compania;

    @OneToMany(mappedBy = "videojuego", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<VideojuegoGenero> generos = new HashSet<>();

    @OneToMany(mappedBy = "videojuego")
    @Builder.Default
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "videojuego", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<VideojuegoIdioma> idiomas = new HashSet<>();

    @OneToMany(mappedBy = "videojuego", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<VideojuegoPlataforma> plataformas = new HashSet<>();

    @OneToMany(mappedBy = "videojuego", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<VideojuegoMultimedia> videojuegoMultimedia = new HashSet<>();
}