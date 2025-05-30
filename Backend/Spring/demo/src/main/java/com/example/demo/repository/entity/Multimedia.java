package com.example.demo.repository.entity;


import java.time.LocalDateTime;
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
@Table(name = "multimedia")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Multimedia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String tipoContenido;
    private LocalDateTime fechaSubida;
    private String nombre;
    private String descripcion;

    @OneToMany(mappedBy = "multimedia", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PostMultimedia> postMultimedia = new HashSet<>();

    @OneToMany(mappedBy = "multimedia", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<VideojuegoMultimedia> videojuegoMultimedia = new HashSet<>();
}