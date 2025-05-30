package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Post;
import com.example.demo.repository.entity.Reaccion;
import com.example.demo.repository.entity.Usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReaccionDTO {
    private Long id;
    private Post idPost;
    private Usuario idUsuario;
    private String tipo;
    private java.time.LocalDateTime fechaCreacion;

    public static ReaccionDTO convertToDTO(Reaccion entity) {
        if (entity == null) return null;
        return ReaccionDTO.builder()
                .id(entity.getId())
                .idPost(entity.getPost())
                .idUsuario(entity.getUsuario())
                .tipo(entity.getTipo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public static Reaccion convertToEntity(ReaccionDTO dto) {
        if (dto == null) return null;
        Reaccion entity = new Reaccion();
        entity.setId(dto.getId());
        entity.setPost(dto.getIdPost());
        entity.setUsuario(dto.getIdUsuario());
        entity.setTipo(dto.getTipo());
        entity.setFechaCreacion(dto.getFechaCreacion());
        return entity;
    }

    public static Set<ReaccionDTO> convertSetToDto(Set<Reaccion> entities) {
        return entities == null ? null : entities.stream()
                .map(ReaccionDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Reaccion> convertSetToEntity(Set<ReaccionDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(ReaccionDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}
