package com.example.demo.repository.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "videojuegos_plataforma")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VideojuegoPlataforma {
    @EmbeddedId
    private VideojuegoPlataformaId id;

    @ManyToOne
    @MapsId("plataformaId")
    @JoinColumn(name = "id_plataforma",referencedColumnName = "id")
    private Plataforma plataforma;

    @ManyToOne
    @MapsId("videojuegoId")
    @JoinColumn(name = "id_videojuego", referencedColumnName = "id")
    private Videojuego videojuego;
}