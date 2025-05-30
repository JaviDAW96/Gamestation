package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Etiqueta;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EtiquetaDTO {
    private Long id;
    private String nombre;

    public static EtiquetaDTO convertToDTO(Etiqueta entity) {
        if (entity == null) return null;
        return EtiquetaDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }

    public static Etiqueta convertToEntity(EtiquetaDTO dto) {
        if (dto == null) return null;
        Etiqueta entity = new Etiqueta();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        return entity;
    }

    public static Set<EtiquetaDTO> convertSetToDto(Set<Etiqueta> entities) {
        return entities == null ? null : entities.stream()
                .map(EtiquetaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Etiqueta> convertSetToEntity(Set<EtiquetaDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(EtiquetaDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}
