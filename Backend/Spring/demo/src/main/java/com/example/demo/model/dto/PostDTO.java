package com.example.demo.model.dto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.repository.entity.Comentario;
import com.example.demo.repository.entity.Post;
import com.example.demo.repository.entity.PostCategoria;
import com.example.demo.repository.entity.PostEtiqueta;
import com.example.demo.repository.entity.PostMultimedia;
import com.example.demo.repository.entity.PostMultimediaId;
import com.example.demo.repository.entity.Reaccion;

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
        private Set<PostEtiquetaDTO> etiquetas;
        private Set<PostCategoriaDTO> categorias;
        private LocalDate fechaPublicacion;
        private Set<Long> multimediaIds;
        private MultimediaDTO miniatura;
        private MultimediaDTO portada; 
        private MultimediaDTO imagenContenido;
        private Set<MultimediaDTO> imagenes;
        private Set<MultimediaDTO> multimedia;
        private Set<PostMultimediaDTO> postMultimedia;
        private Long miniaturaId;
        private Long portadaId;
        private Long imagenContenidoId;
        private MultimediaDTO imagenContenido1;
        private MultimediaDTO imagenContenido2;
        private MultimediaDTO imagenContenido3;
        private Long imagenContenido1Id;
        private Long imagenContenido2Id;
        private Long imagenContenido3Id;

        public static PostDTO convertToDTO(Post post) {
                if (post == null)
                        return null;

                // 1) Copia defensiva de postMultimedia
                Set<PostMultimedia> postMultimediaSafe = post.getPostMultimedia() == null
                                ? Collections.emptySet()
                                : new HashSet<>(post.getPostMultimedia());

                Set<MultimediaDTO> multimediaSet = MultimediaDTO.fromPostMultimedia(postMultimediaSafe);

                Set<MultimediaDTO> imagenes = multimediaSet.stream()
                                .filter(m -> m.getTipoContenido() != null && m.getTipoContenido().startsWith("image"))
                                .collect(Collectors.toSet());

                MultimediaDTO portada = imagenes.stream().findFirst().orElse(null);
                MultimediaDTO miniatura = imagenes.stream()
                                .skip(imagenes.isEmpty() ? 0 : (int) (Math.random() * imagenes.size()))
                                .findFirst()
                                .orElse(portada);

                // 4) Copia defensiva de comentarios
                Set<ComentarioDTO> comentariosDTOs;
                if (post.getComentarios() != null) {
                        Set<Comentario> comentariosSafe = new HashSet<>(post.getComentarios());
                        comentariosDTOs = comentariosSafe.stream()
                                        .map(ComentarioDTO::convertToDTO)
                                        .collect(Collectors.toSet());
                } else {
                        comentariosDTOs = Collections.emptySet();
                }

                // 5) Copia defensiva de reacciones
                Set<ReaccionDTO> reaccionesDTOs;
                if (post.getReacciones() != null) {
                        Set<Reaccion> reaccionesSafe = new HashSet<>(post.getReacciones());
                        reaccionesDTOs = reaccionesSafe.stream()
                                        .map(ReaccionDTO::convertToDTO)
                                        .collect(Collectors.toSet());
                } else {
                        reaccionesDTOs = Collections.emptySet();
                }

                // 6) Copia defensiva de etiquetas
                Set<PostEtiquetaDTO> etiquetasDTOs;
                if (post.getEtiquetas() != null) {
                        Set<PostEtiqueta> etiquetasSafe = new HashSet<>(post.getEtiquetas());
                        etiquetasDTOs = etiquetasSafe.stream()
                                        .map(pe -> PostEtiquetaDTO.builder()
                                                        .idPost(pe.getPost().getId())
                                                        .idEtiqueta(pe.getEtiqueta().getId())
                                                        .build())
                                        .collect(Collectors.toSet());
                } else {
                        etiquetasDTOs = Collections.emptySet();
                }

                // 7) Copia defensiva de categorías
                Set<PostCategoriaDTO> categoriasDTOs;
                if (post.getCategorias() != null) {
                        Set<PostCategoria> categoriasSafe = new HashSet<>(post.getCategorias());
                        categoriasDTOs = categoriasSafe.stream()
                                        .map(pc -> PostCategoriaDTO.builder()
                                                        .idPost(pc.getPost().getId())
                                                        .idCategoria(pc.getCategoria().getId())
                                                        .build())
                                        .collect(Collectors.toSet());
                } else {
                        categoriasDTOs = Collections.emptySet();
                }

                // 8) PostMultimedia DTOs
                Set<PostMultimediaDTO> postMultimediaDTOs = postMultimediaSafe.stream()
                                .map(PostMultimediaDTO::fromEntity)
                                .collect(Collectors.toSet());

                // 9) Construcción final
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
                                .comentarios(comentariosDTOs)
                                .reacciones(reaccionesDTOs)
                                .etiquetas(etiquetasDTOs)
                                .categorias(categoriasDTOs)
                                .multimediaIds(multimediaSet.stream().map(MultimediaDTO::getId)
                                                .collect(Collectors.toSet()))
                                .miniatura(post.getMiniatura() != null ? MultimediaDTO.convertToDTO(post.getMiniatura())
                                                : null)
                                .portada(post.getPortada() != null ? MultimediaDTO.convertToDTO(post.getPortada())
                                                : null)
                                // .imagenContenido(post.getImagenContenido() != null ?
                                // MultimediaDTO.convertToDTO(post.getImagenContenido()) : null)
                                .imagenContenido(null) // FIX: Set to null or provide a valid MultimediaDTO if available
                                .imagenes(imagenes)
                                .multimedia(multimediaSet)
                                .postMultimedia(postMultimediaDTOs)
                                .imagenContenido1(post.getImagenContenido1() != null ? MultimediaDTO.convertToDTO(post.getImagenContenido1()) : null)
                                .imagenContenido2(post.getImagenContenido2() != null ? MultimediaDTO.convertToDTO(post.getImagenContenido2()) : null)
                                .imagenContenido3(post.getImagenContenido3() != null ? MultimediaDTO.convertToDTO(post.getImagenContenido3()) : null)
                                .build();
        }

        public static Post convertToEntity(PostDTO dto,
                        java.util.function.Function<Long, com.example.demo.repository.entity.Usuario> fetchUsuario,
                        java.util.function.Function<Long, com.example.demo.repository.entity.Categoria> fetchCategoria,
                        java.util.function.Function<Long, com.example.demo.repository.entity.Etiqueta> fetchEtiqueta,
                        java.util.function.Function<Long, com.example.demo.repository.entity.Multimedia> fetchMultimedia) {
                if (dto == null)
                        return null;

                Post.PostBuilder builder = Post.builder()
                                .usuario(fetchUsuario.apply(dto.getUsuarioId()))
                                .titulo(dto.getTitulo())
                                .subtitulo(dto.getSubtitulo())
                                .descripcion(dto.getDescripcion())
                                .contenido(dto.getContenido())
                                .tipo(dto.getTipo())
                                .fechaPublicacion(dto.getFechaPublicacion());

                // Asigna las imágenes principales ANTES de build()
                if (dto.getMiniaturaId() != null) {
                        builder.miniatura(fetchMultimedia.apply(dto.getMiniaturaId()));
                }
                if (dto.getPortadaId() != null) {
                        builder.portada(fetchMultimedia.apply(dto.getPortadaId()));
                }
                if (dto.getImagenContenidoId() != null) {
                       
                }

                // Solo asigna el id si es edición (id != null)
                if (dto.getId() != null) {
                        builder.id(dto.getId());
                }

                Post post = builder.build();

                // Nota, Comentarios y Reacciones omitidos para brevedad

                // Relaciones etiquetas
                if (dto.getEtiquetas() != null) {
                        dto.getEtiquetas().forEach(peDto -> {
                                com.example.demo.repository.entity.Etiqueta etiqueta = fetchEtiqueta
                                                .apply(peDto.getIdEtiqueta());
                                com.example.demo.repository.entity.PostEtiqueta pe = new com.example.demo.repository.entity.PostEtiqueta();
                                pe.setId(new com.example.demo.repository.entity.PostEtiquetaId(post.getId(),
                                                etiqueta.getId()));
                                pe.setPost(post);
                                pe.setEtiqueta(etiqueta);
                                post.getEtiquetas().add(pe);
                        });
                }

                // Relaciones categorias
                if (dto.getCategorias() != null) {
                        dto.getCategorias().forEach(pcDto -> {
                                com.example.demo.repository.entity.Categoria categoria = fetchCategoria
                                                .apply(pcDto.getIdCategoria());
                                com.example.demo.repository.entity.PostCategoria pc = new com.example.demo.repository.entity.PostCategoria();
                                pc.setId(new com.example.demo.repository.entity.PostCategoriaId(post.getId(),
                                                categoria.getId()));
                                pc.setPost(post);
                                pc.setCategoria(categoria);
                                post.getCategorias().add(pc);
                        });
                }

                // Relaciones multimedia (N a N)
                if (dto.getPostMultimedia() != null && !dto.getPostMultimedia().isEmpty()) {
                        dto.getPostMultimedia().forEach(pmDto -> {
                                if (pmDto == null)
                                        return; // <-- Añade este control
                                com.example.demo.repository.entity.Multimedia multimedia;
                                if (pmDto.getMultimediaId() != null) {
                                        System.out.println("Buscando multimedia con ID: " + pmDto.getMultimediaId());
                                        multimedia = fetchMultimedia.apply(pmDto.getMultimediaId());
                                } else if (pmDto.getMultimedia() != null) {
                                        System.out.println("Creando nueva multimedia desde DTO");
                                        multimedia = MultimediaDTO.convertToEntity(pmDto.getMultimedia()); // <--- solo
                                                                                                           // pasa el
                                                                                                           // DTO
                                } else {
                                        System.out.println("WARNING: pmDto sin multimediaId ni multimedia");
                                        return; // <-- Sal de la iteración si no hay datos
                                }
                                if (multimedia == null)
                                        return;

                                com.example.demo.repository.entity.PostMultimedia pm = new com.example.demo.repository.entity.PostMultimedia();
                                // Solo arma el ID compuesto si ambos IDs existen
                                if (multimedia.getId() != null && post.getId() != null) {
                                        PostMultimediaId pmId = new PostMultimediaId();
                                        pmId.setPostId(post.getId());
                                        pmId.setMultimediaId(multimedia.getId());
                                        pm.setId(pmId);
                                }
                                // Asocia entidades
                                pm.setPost(post);
                                pm.setMultimedia(multimedia);
                                post.getPostMultimedia().add(pm);
                        });
                } else if (dto.getMultimediaIds() != null) {
                        dto.getMultimediaIds().forEach(multimediaId -> {
                                if (multimediaId == null) {
                                        throw new IllegalArgumentException("multimediaId en multimediaIds es null");
                                }
                                com.example.demo.repository.entity.Multimedia multimedia = fetchMultimedia
                                                .apply(multimediaId);
                                if (multimedia == null) {
                                        throw new IllegalArgumentException(
                                                        "No se encontró multimedia con ID: " + multimediaId);
                                }
                                PostMultimedia pm = new PostMultimedia();
                                PostMultimediaId pmId = new PostMultimediaId();
                                pmId.setPostId(post.getId());
                                pmId.setMultimediaId(multimedia.getId());
                                pm.setId(pmId);
                                pm.setPost(post);
                                pm.setMultimedia(multimedia);
                                post.getPostMultimedia().add(pm);
                        });
                }

                // Nuevas relaciones para miniatura, portada e imagen de contenido
                if (dto.getMiniaturaId() != null) {
                        builder.miniatura(fetchMultimedia.apply(dto.getMiniaturaId()));
                }
                if (dto.getPortadaId() != null) {
                        builder.portada(fetchMultimedia.apply(dto.getPortadaId()));
                }
                // if (dto.getImagenContenidoId() != null) {
                // builder.imagenContenido(fetchMultimedia.apply(dto.getImagenContenidoId()));
                // }
                if (dto.getImagenContenido1Id() != null) {
                        builder.imagenContenido1(fetchMultimedia.apply(dto.getImagenContenido1Id()));
                }
                if (dto.getImagenContenido2Id() != null) {
                        builder.imagenContenido2(fetchMultimedia.apply(dto.getImagenContenido2Id()));
                }
                if (dto.getImagenContenido3Id() != null) {
                        builder.imagenContenido3(fetchMultimedia.apply(dto.getImagenContenido3Id()));
                }

                System.out.println("PostMultimedia size: " + post.getPostMultimedia().size());
                for (PostMultimedia pm : post.getPostMultimedia()) {
                        System.out.println("PM id: " + pm.getId() + " multimedia: "
                                        + (pm.getMultimedia() != null ? pm.getMultimedia().getId() : "null"));
                }

                return post;
        }
}