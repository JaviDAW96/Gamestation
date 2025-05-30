package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Multimedia;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultimediaDTO {
    private Long id;
    private String url;
    private String tipoContenido;
    private java.time.LocalDateTime fechaSubida;
    private String nombre;
    private String descripcion;

    public static MultimediaDTO convertToDTO(Multimedia entity) {
        if (entity == null) return null;
        return MultimediaDTO.builder()
                .id(entity.getId())
                .url(entity.getUrl())
                .tipoContenido(entity.getTipoContenido())
                .fechaSubida(entity.getFechaSubida())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .build();
    }

    public static Multimedia convertToEntity(MultimediaDTO dto) {
        if (dto == null) return null;
        Multimedia entity = new Multimedia();
        entity.setId(dto.getId());
        entity.setUrl(dto.getUrl());
        entity.setTipoContenido(dto.getTipoContenido());
        entity.setFechaSubida(dto.getFechaSubida());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }

    public static Set<MultimediaDTO> convertSetToDto(Set<Multimedia> entities) {
        return entities == null ? null : entities.stream()
                .map(MultimediaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Multimedia> convertSetToEntity(Set<MultimediaDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(MultimediaDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}