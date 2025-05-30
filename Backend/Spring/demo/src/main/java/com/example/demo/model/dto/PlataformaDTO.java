package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Plataforma;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlataformaDTO {
    private Long id;
    private String nombre;

    public static PlataformaDTO convertToDTO(Plataforma entity) {
        if (entity == null) return null;
        return PlataformaDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }

    public static Plataforma convertToEntity(PlataformaDTO dto) {
        if (dto == null) return null;
        Plataforma entity = new Plataforma();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        return entity;
    }

    public static Set<PlataformaDTO> convertSetToDto(Set<Plataforma> entities) {
        return entities == null ? null : entities.stream()
                .map(PlataformaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Plataforma> convertSetToEntity(Set<PlataformaDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(PlataformaDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}