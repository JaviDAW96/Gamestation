package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Genero;
import com.example.demo.repository.entity.Videojuego;
import com.example.demo.repository.entity.VideojuegoGenero;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideojuegoGeneroDTO {
    private Videojuego idVideojuego;
    private Genero idGenero;

    public static VideojuegoGeneroDTO convertToDTO(VideojuegoGenero entity) {
        if (entity == null) return null;
        return VideojuegoGeneroDTO.builder()
                .idVideojuego(entity.getVideojuego())
                .idGenero(entity.getGenero())
                .build();
    }

    public static VideojuegoGenero convertToEntity(VideojuegoGeneroDTO dto) {
        if (dto == null) return null;
        VideojuegoGenero entity = new VideojuegoGenero();
        entity.setVideojuego(dto.getIdVideojuego());
        entity.setGenero(dto.getIdGenero());
        return entity;
    }

    public static Set<VideojuegoGeneroDTO> convertSetToDto(Set<VideojuegoGenero> entities) {
        return entities == null ? null : entities.stream()
                .map(VideojuegoGeneroDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<VideojuegoGenero> convertSetToEntity(Set<VideojuegoGeneroDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(VideojuegoGeneroDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}
