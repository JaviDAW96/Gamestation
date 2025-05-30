package com.example.demo.model.dto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Post;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDTO {
    private Long id;
    private Long usuarioId;
    private String titulo;
    private String subtitulo;
    private String descripcion;
    private String contenido;
    private String tipo;
    private NotaDTO nota;
    private Set<ComentarioDTO> comentarios;
    private Set<ReaccionDTO> reacciones;
    // Ahora solo IDs de relación
    private Set<PostEtiquetaDTO> etiquetas;
    private Set<PostCategoriaDTO> categorias;
    private LocalDate fechaPublicacion;
    private Set<Long> multimediaIds; // IDs de multimedia asociados

    public static PostDTO convertToDTO(Post post) {
        if (post == null)
            return null;

        return PostDTO.builder()
                .id(post.getId())
                .usuarioId(post.getUsuario() != null ? post.getUsuario().getId() : null)
                .titulo(post.getTitulo())
                .subtitulo(post.getSubtitulo())
                .descripcion(post.getDescripcion())
                .contenido(post.getContenido())
                .tipo(post.getTipo())
                .fechaPublicacion(post.getFechaPublicacion())
                .nota(NotaDTO.convertToDTO(post.getNota()))
                .comentarios(post.getComentarios() != null
                        ? post.getComentarios().stream()
                                .map(ComentarioDTO::convertToDTO)
                                .collect(Collectors.toSet())
                        : Collections.emptySet())
                .reacciones(post.getReacciones() != null
                        ? post.getReacciones().stream()
                                .map(ReaccionDTO::convertToDTO)
                                .collect(Collectors.toSet())
                        : Collections.emptySet())
                .etiquetas(post.getEtiquetas() != null
                        ? post.getEtiquetas().stream()
                                .map(pe -> PostEtiquetaDTO.builder()
                                        .idPost(pe.getPost().getId())
                                        .idEtiqueta(pe.getEtiqueta().getId())
                                        .build())
                                .collect(Collectors.toSet())
                        : Collections.emptySet())
                .categorias(post.getCategorias() != null
                        ? post.getCategorias().stream()
                                .map(pc -> PostCategoriaDTO.builder()
                                        .idPost(pc.getPost().getId())
                                        .idCategoria(pc.getCategoria().getId())
                                        .build())
                                .collect(Collectors.toSet())
                        : Collections.emptySet())
                .multimediaIds(
                        post.getPostMultimedia() != null
                                ? post.getPostMultimedia().stream()
                                        .map(pm -> pm.getMultimedia().getId())
                                        .collect(Collectors.toSet())
                                : Collections.emptySet())
                .build();
    }

    public static Post convertToEntity(PostDTO dto,
            java.util.function.Function<Long, com.example.demo.repository.entity.Usuario> fetchUsuario,
            java.util.function.Function<Long, com.example.demo.repository.entity.Categoria> fetchCategoria,
            java.util.function.Function<Long, com.example.demo.repository.entity.Etiqueta> fetchEtiqueta,
            java.util.function.Function<Long, com.example.demo.repository.entity.Multimedia> fetchMultimedia) {
        if (dto == null)
            return null;

        Post post = Post.builder()
                .id(dto.getId())
                .usuario(fetchUsuario.apply(dto.getUsuarioId()))
                .titulo(dto.getTitulo())
                .subtitulo(dto.getSubtitulo())
                .descripcion(dto.getDescripcion())
                .contenido(dto.getContenido())
                .tipo(dto.getTipo())
                .fechaPublicacion(dto.getFechaPublicacion())
                .build();

        // Nota, Comentarios y Reacciones omitidos para brevedad

        // Relaciones etiquetas
        if (dto.getEtiquetas() != null) {
            dto.getEtiquetas().forEach(peDto -> {
                com.example.demo.repository.entity.Etiqueta etiqueta = fetchEtiqueta.apply(peDto.getIdEtiqueta());
                com.example.demo.repository.entity.PostEtiqueta pe = new com.example.demo.repository.entity.PostEtiqueta();
                pe.setId(new com.example.demo.repository.entity.PostEtiquetaId(post.getId(), etiqueta.getId()));
                pe.setPost(post);
                pe.setEtiqueta(etiqueta);
                post.getEtiquetas().add(pe);
            });
        }

        // Relaciones categorias
        if (dto.getCategorias() != null) {
            dto.getCategorias().forEach(pcDto -> {
                com.example.demo.repository.entity.Categoria categoria = fetchCategoria.apply(pcDto.getIdCategoria());
                com.example.demo.repository.entity.PostCategoria pc = new com.example.demo.repository.entity.PostCategoria();
                pc.setId(new com.example.demo.repository.entity.PostCategoriaId(post.getId(), categoria.getId()));
                pc.setPost(post);
                pc.setCategoria(categoria);
                post.getCategorias().add(pc);
            });
        }

        // Relaciones multimedia (N a N)
        if (dto.getMultimediaIds() != null) {
            dto.getMultimediaIds().forEach(multimediaId -> {
                com.example.demo.repository.entity.Multimedia multimedia = fetchMultimedia.apply(multimediaId);
                com.example.demo.repository.entity.PostMultimedia pm = new com.example.demo.repository.entity.PostMultimedia();
                com.example.demo.repository.entity.PostMultimediaId pmId = new com.example.demo.repository.entity.PostMultimediaId();
                pmId.setPostId(post.getId());
                pmId.setMultimediaId(multimedia.getId());
                pm.setId(pmId);
                pm.setPost(post);
                pm.setMultimedia(multimedia);
                post.getPostMultimedia().add(pm);
            });
        }

        return post;
    }
}