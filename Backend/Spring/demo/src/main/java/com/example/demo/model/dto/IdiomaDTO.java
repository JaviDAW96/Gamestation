package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Idioma;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdiomaDTO {
    private Long id;
    private String nombre;

    public static IdiomaDTO convertToDTO(Idioma entity) {
        if (entity == null) return null;
        return IdiomaDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }

    public static Idioma convertToEntity(IdiomaDTO dto) {
        if (dto == null) return null;
        Idioma entity = new Idioma();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        return entity;
    }

    public static Set<IdiomaDTO> convertSetToDto(Set<Idioma> entities) {
        return entities == null ? null : entities.stream()
                .map(IdiomaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Idioma> convertSetToEntity(Set<IdiomaDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(IdiomaDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}