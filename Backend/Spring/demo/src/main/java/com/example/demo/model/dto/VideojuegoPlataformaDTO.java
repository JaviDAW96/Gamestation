package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Plataforma;
import com.example.demo.repository.entity.Videojuego;
import com.example.demo.repository.entity.VideojuegoPlataforma;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideojuegoPlataformaDTO {

    private Plataforma idPlataforma;
    private Videojuego idVideojuego;

    public static VideojuegoPlataformaDTO convertToDTO(VideojuegoPlataforma entity) {
        return entity == null ? null : VideojuegoPlataformaDTO.builder()
                .idPlataforma(entity.getPlataforma())
                .idVideojuego(entity.getVideojuego())
                .build();
    }

    public static VideojuegoPlataforma convertToEntity(VideojuegoPlataformaDTO dto) {
        return dto == null ? null : VideojuegoPlataforma.builder()
                .plataforma(dto.getIdPlataforma())
                .videojuego(dto.getIdVideojuego())
                .build();
    }

    public static Set<VideojuegoPlataformaDTO> convertSetToDTO(Set<VideojuegoPlataforma> entities) {
        return entities == null ? null : entities.stream()
                .map(VideojuegoPlataformaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<VideojuegoPlataforma> convertSetToEntity(Set<VideojuegoPlataformaDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(VideojuegoPlataformaDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}
