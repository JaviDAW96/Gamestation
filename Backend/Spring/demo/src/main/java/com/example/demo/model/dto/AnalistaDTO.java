package com.example.demo.model.dto;

import com.example.demo.repository.entity.Analista;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Analista DTO
@Data
@Builder
public class AnalistaDTO {
    private Long id;
    private Long idGenero;
    private String descripcion;
    private String experienciaLaboral;
    private Integer noticiasPublicadas;
    private UsuarioDTO usuario;
    private List<PostDTO> posts;

    public static AnalistaDTO convertToDTO(Analista entity) {
        if (entity == null) return null;
        return AnalistaDTO.builder()
                .id(entity.getId())
                .descripcion(entity.getDescripcion())
                .experienciaLaboral(entity.getExperienciaLaboral())
                .noticiasPublicadas(entity.getNoticiasPublicadas())
                .build();
    }

    public static AnalistaDTO convertToDTO(Analista entity, UsuarioDTO usuario, List<PostDTO> posts) {
        if (entity == null) return null;
        return AnalistaDTO.builder()
                .id(entity.getId())
                .descripcion(entity.getDescripcion())
                .experienciaLaboral(entity.getExperienciaLaboral())
                .noticiasPublicadas(entity.getNoticiasPublicadas())
                .usuario(usuario)
                .posts(posts)
                .build();
    }

    public static Analista convertToEntity(AnalistaDTO dto) {
        if (dto == null) return null;
        Analista entity = new Analista();
        entity.setId(dto.getId());
        entity.setDescripcion(dto.getDescripcion());
        entity.setExperienciaLaboral(dto.getExperienciaLaboral());
        entity.setNoticiasPublicadas(dto.getNoticiasPublicadas());
        return entity;
    }

    public static Set<AnalistaDTO> convertSetToDto(Set<Analista> entities) {
        return entities == null ? null : entities.stream()
                .map(AnalistaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<Analista> convertSetToEntity(Set<AnalistaDTO> dtos) {
        return dtos == null ? null : dtos.stream()
                .map(AnalistaDTO::convertToEntity)
                .collect(Collectors.toSet());
    }
}
