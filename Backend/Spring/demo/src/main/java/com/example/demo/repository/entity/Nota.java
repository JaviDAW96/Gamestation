package com.example.demo.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Nota {
    @Id
    private Long id; 
    private String nombre;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Post post;
}