package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Comentario;
import com.example.demo.repository.entity.Post;
import com.example.demo.repository.entity.Usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComentarioDTO {
    private Long id;
    private Post idPost;
    private Usuario idUsuario;
    private String contenido;
    private java.time.LocalDateTime fechaCreacion;

    public static ComentarioDTO convertToDTO(Comentario entity) {
        if (entity == null) return null;
        return ComentarioDTO.builder()
                .id(entity.getId())
                .idPost(entity.getPost())
                .idUsuario(entity.getUsuario())
                .contenido(entity.getContenido())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static Comentario convertToEntity(ComentarioDTO dto) {
        if (dto == null) return null;
        Comentario entity = new Comentario();
        entity.setId(dto.getId());
        entity.setPost(dto.getIdPost());
        entity.setUsuario(dto.getIdUsuario());
        entity.setContenido(dto.getContenido());
        entity.setFechaCreacion(dto.getFechaCreacion());
        return entity;
    }

    public static Set<ComentarioDTO> convertSetToDto(Set<Comentario> entities) {
        return entities == null ? null : entities.stream()
                .map(ComentarioDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Comentario> convertSetToEntity(Set<ComentarioDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(ComentarioDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}