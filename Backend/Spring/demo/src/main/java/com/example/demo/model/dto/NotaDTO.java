package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Nota;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotaDTO {
    private Long id;
    private String nombre;

    public static NotaDTO convertToDTO(Nota entity) {
        if (entity == null) return null;
        return NotaDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }

    public static Nota convertToEntity(NotaDTO dto) {
        if (dto == null) return null;
        Nota entity = new Nota();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        return entity;
    }

    public static Set<NotaDTO> convertSetToDto(Set<Nota> entities) {
        return entities == null ? null : entities.stream()
                .map(NotaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Nota> convertSetToEntity(Set<NotaDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(NotaDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}
