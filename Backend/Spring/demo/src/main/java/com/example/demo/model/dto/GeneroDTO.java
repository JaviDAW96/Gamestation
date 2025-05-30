package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Genero;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneroDTO {
    private Long id;
    private String nombre;

    public static GeneroDTO convertToDTO(Genero entity) {
        if (entity == null) return null;
        return GeneroDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }

    public static Genero convertToEntity(GeneroDTO dto) {
        if (dto == null) return null;
        Genero entity = new Genero();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        return entity;
    }

    public static Set<GeneroDTO> convertSetToDto(Set<Genero> entities) {
        return entities == null ? null : entities.stream()
                .map(GeneroDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Genero> convertSetToEntity(Set<GeneroDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(GeneroDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}
