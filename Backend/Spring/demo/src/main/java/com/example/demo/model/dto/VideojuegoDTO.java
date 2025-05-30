package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Videojuego;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideojuegoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagen;
    private Integer duracion;
    private Integer jugadores;
    private java.time.LocalDate lanzamiento;
    private String compania;
    private Set<Long> multimediaIds;

    public static VideojuegoDTO convertToDTO(Videojuego entity) {
        if (entity == null)
            return null;
        return VideojuegoDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .imagen(entity.getImagen())
                .duracion(entity.getDuracion())
                .jugadores(entity.getJugadores())
                .lanzamiento(entity.getLanzamiento())
                .compania(entity.getCompania())
                .multimediaIds(
                        entity.getVideojuegoMultimedia() != null
                                ? entity.getVideojuegoMultimedia().stream()
                                        .map(vm -> vm.getMultimedia().getId())
                                        .collect(Collectors.toSet())
                                : null)
                .build();
    }

    public static Videojuego convertToEntity(
            VideojuegoDTO dto,
            java.util.function.Function<Long, com.example.demo.repository.entity.Multimedia> fetchMultimedia) {
        if (dto == null)
            return null;
        Videojuego entity = new Videojuego();
        entity.setId(dto.getId());
        entity.setTitulo(dto.getTitulo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setImagen(dto.getImagen());
        entity.setDuracion(dto.getDuracion());
        entity.setJugadores(dto.getJugadores());
        entity.setLanzamiento(dto.getLanzamiento());
        entity.setCompania(dto.getCompania());

        // Relaciones multimedia (N a N)
        if (dto.getMultimediaIds() != null) {
            dto.getMultimediaIds().forEach(multimediaId -> {
                com.example.demo.repository.entity.Multimedia multimedia = fetchMultimedia.apply(multimediaId);
                com.example.demo.repository.entity.VideojuegoMultimedia vm = new com.example.demo.repository.entity.VideojuegoMultimedia();
                com.example.demo.repository.entity.VideojuegoMultimediaId vmId = new com.example.demo.repository.entity.VideojuegoMultimediaId();
                vmId.setVideojuegoId(entity.getId());
                vmId.setMultimediaId(multimedia.getId());
                vm.setId(vmId);
                vm.setVideojuego(entity);
                vm.setMultimedia(multimedia);
                entity.getVideojuegoMultimedia().add(vm);
            });
        }

        return entity;
    }

    public static Set<VideojuegoDTO> convertSetToDto(Set<Videojuego> entities) {
        return entities == null ? null
                : entities.stream()
                        .map(VideojuegoDTO::convertToDTO)
                        .collect(Collectors.toSet());
    }

    public static Set<Videojuego> convertSetToEntity(Set<VideojuegoDTO> dtos, java.util.function.Function<Long, com.example.demo.repository.entity.Multimedia> fetchMultimedia) {
        return dtos == null ? null
                : dtos.stream()
                        .map(dto -> convertToEntity(dto, fetchMultimedia))
                        .collect(Collectors.toSet());
    }
}
