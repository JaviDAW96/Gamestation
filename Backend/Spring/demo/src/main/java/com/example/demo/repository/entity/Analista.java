package com.example.demo.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "analista")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Analista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // PK propia de analista

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;            // FK a usuarios.id

    private String descripcion;
    private String experienciaLaboral;
    private Integer noticiasPublicadas;

}

