package com.example.demo.model.dto;


import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Usuario;


import lombok.Builder;

import lombok.Data;

@Data
@Builder
public class UsuarioDTO {
    private Long id;
    private RolDTO rol;
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private java.time.LocalDate fechaNacimiento;
    private String imagen;
    private String password;

    public static UsuarioDTO convertToDTO(Usuario entity) {
        if (entity == null) return null;
        return UsuarioDTO.builder()
                .id(entity.getId())
                .rol(RolDTO.convertToDTO(entity.getRol()))
                .nombre(entity.getNombre())
                .apellidos(entity.getApellidos())
                .dni(entity.getDni())
                .email(entity.getEmail())
                .fechaNacimiento(entity.getFechaNacimiento())
                .password(entity.getPassword())
                .imagen(entity.getImagen())
                .build();
    }

    public static Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) return null;
        Usuario entity = new Usuario();
        entity.setId(usuarioDTO.getId());
        entity.setRol(RolDTO.convertToEntity(usuarioDTO.getRol()));
        entity.setNombre(usuarioDTO.getNombre());
        entity.setApellidos(usuarioDTO.getApellidos());
        entity.setDni(usuarioDTO.getDni());
        entity.setEmail(usuarioDTO.getEmail());
        entity.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
        entity.setImagen(usuarioDTO.getImagen());
        entity.setPassword(usuarioDTO.getPassword());
        return entity;
    }

    public static Set<UsuarioDTO> convertSetToDto(Set<Usuario> entities) {
        return entities == null ? null : entities.stream()
                .map(UsuarioDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Usuario> convertSetToEntity(Set<UsuarioDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(UsuarioDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}