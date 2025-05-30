package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Rol;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolDTO {
    private Long id;      // Identificador del rol
    private String rol; // Nombre del rol (por ejemplo, "ROLE_USER")

    public static RolDTO convertToDTO(Rol entity) {
        if (entity == null) return null;
        return RolDTO.builder()
                .id(entity.getId())
                .rol(entity.getRol())
                .build();
    }

    public static Rol convertToEntity(RolDTO dto) {
        if (dto == null) return null;
        Rol entity = new Rol();
        entity.setId(dto.getId());
        entity.setRol(dto.getRol());
        return entity;
    }

    public static Set<RolDTO> convertSetToDto(Set<Rol> entities) {
        return entities == null ? null : entities.stream()
                .map(RolDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Rol> convertSetToEntity(Set<RolDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(RolDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}
