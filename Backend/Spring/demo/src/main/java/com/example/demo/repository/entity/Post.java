package com.example.demo.repository.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String titulo;
    private String subtitulo;
    private String descripcion;
    private String contenido;
    private String tipo;
    private LocalDate fechaPublicacion;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Comentario> comentarios = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_videojuego")
    private Videojuego videojuego;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private Nota nota;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Reaccion> reacciones = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PostEtiqueta> etiquetas = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PostCategoria> categorias = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private Set<PostMultimedia> postMultimedia = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "miniatura_id")
    private Multimedia miniatura;

    @ManyToOne
    @JoinColumn(name = "portada_id")
    private Multimedia portada;

    @ManyToOne
    @JoinColumn(name = "imagen_contenido1_id")
    private Multimedia imagenContenido1;

    @ManyToOne
    @JoinColumn(name = "imagen_contenido2_id")
    private Multimedia imagenContenido2;

    @ManyToOne
    @JoinColumn(name = "imagen_contenido3_id")
    private Multimedia imagenContenido3;

}
