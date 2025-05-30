package com.example.demo.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PostEtiquetaId {
    private Long postId;
    private Long etiquetaId;
}