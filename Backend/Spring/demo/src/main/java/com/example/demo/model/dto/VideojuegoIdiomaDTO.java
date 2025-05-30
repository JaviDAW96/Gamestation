package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Idioma;
import com.example.demo.repository.entity.Videojuego;
import com.example.demo.repository.entity.VideojuegoIdioma;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideojuegoIdiomaDTO {

    private Videojuego idVideojuego;
    private Idioma idIdioma;

    public static VideojuegoIdiomaDTO convertToDTO(VideojuegoIdioma videojuegoIdioma) {
        return null == videojuegoIdioma ? null : VideojuegoIdiomaDTO.builder()
        .idVideojuego(videojuegoIdioma.getVideojuego())
        .idIdioma(videojuegoIdioma.getIdioma())
        .build();
    }

    public static VideojuegoIdioma convertToEntity(VideojuegoIdiomaDTO videojuegoIdiomaDTO) {
        return null == videojuegoIdiomaDTO ? null : VideojuegoIdioma.builder()
        .videojuego(videojuegoIdiomaDTO.getIdVideojuego())
        .idioma(videojuegoIdiomaDTO.getIdIdioma())
        .build();
    }

    public static Set<VideojuegoIdiomaDTO> convertSetToDTO(Set<VideojuegoIdioma> entities) {
        return entities == null ? null : entities.stream()
        .map(VideojuegoIdiomaDTO::convertToDTO)
        .collect(Collectors.toSet());
    }

    public static Set<VideojuegoIdioma> convertSetToEntity(Set<VideojuegoIdiomaDTO> dtos) {
        return dtos == null ? null : dtos.stream()
        .map(VideojuegoIdiomaDTO::convertToEntity)
        .collect(Collectors.toSet());
    }

}
