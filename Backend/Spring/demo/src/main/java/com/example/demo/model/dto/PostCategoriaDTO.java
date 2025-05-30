package com.example.demo.model.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Categoria;
import com.example.demo.repository.entity.Post;
import com.example.demo.repository.entity.PostCategoria;
import com.example.demo.repository.entity.PostCategoriaId;

import io.jsonwebtoken.lang.Collections;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class PostCategoriaDTO {
    private Long idPost;
    private Long idCategoria;

    public static PostCategoriaDTO convertToDTO(PostCategoria entity) {
        if (entity == null) return null;
        return PostCategoriaDTO.builder()
                .idPost(entity.getPost().getId())
                .idCategoria(entity.getCategoria().getId())
                .build();
    }

    public static PostCategoria convertToEntity(PostCategoriaDTO dto, Post post, Categoria categoria) {
        if (dto == null) return null;
        PostCategoria entity = new PostCategoria();
        // Clave compuesta
        entity.setId(new PostCategoriaId(dto.getIdPost(), dto.getIdCategoria()));
        // Relaciones
        entity.setPost(post);
        entity.setCategoria(categoria);
        return entity;
    }

    public static Set<PostCategoriaDTO> convertSetToDto(Set<PostCategoria> entities) {
        return entities == null
            ? Collections.emptySet()
            : entities.stream()
                .map(PostCategoriaDTO::convertToDTO)
                .collect(Collectors.toSet());
    }

    public static Set<PostCategoria> convertSetToEntity(Set<PostCategoriaDTO> dtos, Post post, Categoria categoria) {
        if (dtos == null || dtos.isEmpty()) {
            return java.util.Collections.emptySet();
        }
    
        return dtos.stream()
            .map(dto -> {
                PostCategoria entity = new PostCategoria();
                // Clave compuesta
                entity.setId(new PostCategoriaId(dto.getIdPost(), dto.getIdCategoria()));
                // Relaciones
                entity.setPost(post);
                entity.setCategoria(categoria);
                return entity;
            })
            .collect(Collectors.toSet());
    }
}
