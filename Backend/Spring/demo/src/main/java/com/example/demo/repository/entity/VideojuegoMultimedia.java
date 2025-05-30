package com.example.demo.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "videojuegos_multimedia")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VideojuegoMultimedia {
    @EmbeddedId
    private VideojuegoMultimediaId id;

    @ManyToOne
    @MapsId("videojuegoId")
    @JoinColumn(name = "videojuego_id")
    private Videojuego videojuego;

    @ManyToOne
    @MapsId("multimediaId")
    @JoinColumn(name = "multimedia_id")
    private Multimedia multimedia;
}