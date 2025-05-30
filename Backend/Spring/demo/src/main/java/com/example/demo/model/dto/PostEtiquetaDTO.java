package com.example.demo.model.dto;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


import com.example.demo.repository.entity.PostEtiqueta;
import com.example.demo.repository.entity.PostEtiquetaId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostEtiquetaDTO {
    private Long idPost;
    private Long idEtiqueta;

    /**
     * Convierte una entidad PostEtiqueta a DTO usando solo IDs
     */
    public static PostEtiquetaDTO convertToDTO(PostEtiqueta entity) {
        if (entity == null) return null;
        return PostEtiquetaDTO.builder()
                .idPost(entity.getPost().getId())
                .idEtiqueta(entity.getEtiqueta().getId())
                .build();
    }

    /**
     * Convierte este DTO a entidad PostEtiqueta con ID compuesto
     * Nota: las relaciones a Post y Etiqueta pueden completarse en la capa de servicio
     */
    public PostEtiqueta convertToEntity() {
        PostEtiqueta pe = new PostEtiqueta();
        pe.setId(new PostEtiquetaId(this.idPost, this.idEtiqueta));
        // post y etiqueta deberán asignarse externamente:
        // pe.setPost(postEntity);
        // pe.setEtiqueta(etiquetaEntity);
        return pe;
    }

    /**
     * Convierte un conjunto de entidades PostEtiqueta a DTOs
     */
    public static Set<PostEtiquetaDTO> convertSetToDto(Set<PostEtiqueta> entities) {
        return entities == null
            ? Collections.emptySet()
            : entities.stream()
                .map(PostEtiquetaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Convierte un conjunto de DTOs a entidades PostEtiqueta
     */
    public static Set<PostEtiqueta> convertSetToEntity(Set<PostEtiquetaDTO> dtos) {
        return dtos == null
            ? Collections.emptySet()
            : dtos.stream()
                .map(PostEtiquetaDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}
