package com.example.demo.repository.entity;

import lombok.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nombre;
    private String apellidos;
    private String dni;
    
    @Column(name = "email")
    private String email;
    
    private LocalDate fechaNacimiento;
    private String imagen;
    private String password;

    @ManyToOne // Muchos usuarios pueden tener el mismo rol
    @JoinColumn(name = "id_rol")
    @JsonBackReference // Evita la serialización recursiva
    private Rol rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Post> posts = new HashSet<>();

    // Lombok generará equals()/hashCode() basados solo en 'id'.
}
