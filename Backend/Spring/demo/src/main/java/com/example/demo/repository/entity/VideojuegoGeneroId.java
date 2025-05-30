package com.example.demo.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VideojuegoGeneroId {
    private Long videojuegoId;
    private Long generoId;

}