package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Categoria;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriaDTO {
    private Long id;
    private String nombre;

    public static CategoriaDTO convertToDTO(Categoria entity) {
        if (entity == null) return null;
        return CategoriaDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }

    public static Categoria convertToEntity(CategoriaDTO dto) {
        if (dto == null) return null;
        Categoria entity = new Categoria();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        return entity;
    }

    public static Set<CategoriaDTO> convertSetToDto(Set<Categoria> entities) {
        return entities == null ? null : entities.stream()
                .map(CategoriaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Categoria> convertSetToEntity(Set<CategoriaDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(CategoriaDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}