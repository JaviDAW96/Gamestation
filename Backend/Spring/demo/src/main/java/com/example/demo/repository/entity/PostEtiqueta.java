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
@Table(name = "posts_etiquetas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PostEtiqueta {
    @EmbeddedId
    private PostEtiquetaId id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "id_post")
    private Post post;

    @ManyToOne
    @MapsId("etiquetaId")
    @JoinColumn(name = "id_etiqueta")
    private Etiqueta etiqueta;
}