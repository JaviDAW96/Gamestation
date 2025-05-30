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
@Table(name = "post_categorias")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PostCategoria {
    @EmbeddedId
    private PostCategoriaId id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "id_post")
    private Post post;

    @ManyToOne
    @MapsId("categoriaId")
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
}