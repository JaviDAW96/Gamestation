package com.example.demo.model.dto;


import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


import com.example.demo.repository.entity.Multimedia;
import com.example.demo.repository.entity.PostMultimedia;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MultimediaDTO {
    private Long id;
    private String url;
    private String tipoContenido;
    private java.time.LocalDateTime fechaSubida;
    private String nombre;
    private String descripcion;

    // Constructor para deserializar desde un String (la URL)
    public MultimediaDTO(String url) {
        this.url = url;
    }

    public MultimediaDTO() {} // Constructor vacío requerido por Jackson

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

      public static void logPostMultimedia(Set<PostMultimedia> postMultimediaSet) {
        if (postMultimediaSet != null) {
            for (PostMultimedia pm : postMultimediaSet) {
                if (pm.getMultimedia() == null) {
                    System.err.println("¡Cuidado! PostMultimedia con id " + pm.getId() + " tiene multimedia null");
                } else {
                    System.out.println("Multimedia asociada: " + pm.getMultimedia().getId() + " - " + pm.getMultimedia().getUrl());
                }
            }
        }
    }
    
    public static Set<MultimediaDTO> fromPostMultimedia(Set<PostMultimedia> postMultimediaSet) {
        if (postMultimediaSet == null) return Collections.emptySet();
        return postMultimediaSet.stream()
            .filter(pm -> pm != null && pm.getMultimedia() != null)
            .map(pm -> MultimediaDTO.convertToDTO(pm.getMultimedia()))
            .collect(Collectors.toSet());
    }

    public static Set<PostMultimedia> filtrarPostMultimedia(Set<PostMultimedia> postMultimedia) {
        return postMultimedia == null ? null : postMultimedia.stream()
                .filter(pm -> pm.getMultimedia() != null)
                .collect(Collectors.toSet());
    }

    public static Set<MultimediaDTO> filtrarImagenes(Set<MultimediaDTO> multimediaSet) {
        return multimediaSet == null ? null : multimediaSet.stream()
                .filter(m -> m != null && m.getTipoContenido() != null && m.getTipoContenido().startsWith("image"))
                .collect(Collectors.toSet());
    }
}