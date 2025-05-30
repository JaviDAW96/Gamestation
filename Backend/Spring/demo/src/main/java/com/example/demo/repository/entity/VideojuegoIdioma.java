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
@Table(name = "videojuegos_idioma")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VideojuegoIdioma {
    @EmbeddedId
    private VideojuegoIdiomaId id;

    @ManyToOne
    @MapsId("videojuegoId")
    @JoinColumn(name = "id_videojuego", referencedColumnName = "id")
    private Videojuego videojuego;

    @ManyToOne
    @MapsId("idiomaId")
    @JoinColumn(name = "id_idioma", referencedColumnName = "id")
    private Idioma idioma;
}