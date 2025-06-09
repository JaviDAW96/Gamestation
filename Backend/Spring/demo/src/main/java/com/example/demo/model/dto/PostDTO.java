package com.example.demo.model.dto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
        private List<PostMultimediaDTO> imagenes;
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

                List<MultimediaDTO> multimediaImagenes = multimediaSet.stream()
                                .filter(m -> m.getTipoContenido() != null && m.getTipoContenido().startsWith("image"))
                                .collect(Collectors.toList());

                // Convert MultimediaDTO list to PostMultimediaDTO list if needed
                // 2) Construye la lista de imagenes con rol desde la relación PostMultimedia
                List<PostMultimediaDTO> imagenes = postMultimediaSafe.stream()
                                .map(PostMultimediaDTO::fromEntity)
                                .collect(Collectors.toList());

                MultimediaDTO portada = null;
                MultimediaDTO miniatura = null;
                MultimediaDTO imagenContenido1 = null;
                MultimediaDTO imagenContenido2 = null;
                MultimediaDTO imagenContenido3 = null;

                for (PostMultimedia pm : postMultimediaSafe) {
                        if ("miniatura".equals(pm.getRol())) {
                                miniatura = MultimediaDTO.convertToDTO(pm.getMultimedia());
                        } else if ("portada".equals(pm.getRol())) {
                                portada = MultimediaDTO.convertToDTO(pm.getMultimedia());
                        } else if ("contenido1".equals(pm.getRol())) {
                                imagenContenido1 = MultimediaDTO.convertToDTO(pm.getMultimedia());
                        } else if ("contenido2".equals(pm.getRol())) {
                                imagenContenido2 = MultimediaDTO.convertToDTO(pm.getMultimedia());
                        } else if ("contenido3".equals(pm.getRol())) {
                                imagenContenido3 = MultimediaDTO.convertToDTO(pm.getMultimedia());
                        }
                }

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
                                .miniatura(miniatura)
                                .miniaturaId(miniatura != null ? miniatura.getId() : null)
                                .portada(portada)
                                .portadaId(portada != null ? portada.getId() : null)
                                .imagenContenido1(imagenContenido1)
                                .imagenContenido1Id(imagenContenido1 != null ? imagenContenido1.getId() : null)
                                .imagenContenido2(imagenContenido2)
                                .imagenContenido2Id(imagenContenido2 != null ? imagenContenido2.getId() : null)
                                .imagenContenido3(imagenContenido3)
                                .imagenContenido3Id(imagenContenido3 != null ? imagenContenido3.getId() : null)
                                .imagenes(imagenes)
                                .multimedia(multimediaSet)
                                .postMultimedia(postMultimediaDTOs)
                                .build();
        }

        public static Post convertToEntity(PostDTO dto,
                        java.util.function.Function<Long, com.example.demo.repository.entity.Usuario> fetchUsuario,
                        java.util.function.Function<Long, com.example.demo.repository.entity.Categoria> fetchCategoria,
                        java.util.function.Function<Long, com.example.demo.repository.entity.Etiqueta> fetchEtiqueta,
                        java.util.function.Function<Long, com.example.demo.repository.entity.Multimedia> fetchMultimedia,
                        java.util.function.Function<Long, com.example.demo.repository.entity.Post> fetchPost // <--
                                                                                                             // Añade
                                                                                                             // esto
        ) {
                if (dto == null)
                        return null;

                LocalDate fechaPublicacion = dto.getFechaPublicacion();
                if (fechaPublicacion == null && dto.getId() != null) {
                        Post original = fetchPost.apply(dto.getId());
                        if (original != null) {
                                fechaPublicacion = original.getFechaPublicacion();
                        }
                }

                Post.PostBuilder builder = Post.builder()
                                .usuario(fetchUsuario.apply(dto.getUsuarioId()))
                                .titulo(dto.getTitulo())
                                .subtitulo(dto.getSubtitulo())
                                .descripcion(dto.getDescripcion())
                                .contenido(dto.getContenido())
                                .tipo(dto.getTipo())
                                .fechaPublicacion(fechaPublicacion);

                if (dto.getMiniaturaId() != null) {
                        var miniatura = fetchMultimedia.apply(dto.getMiniaturaId());
                        if (miniatura != null)
                                builder.miniatura(miniatura);
                }
                if (dto.getPortadaId() != null) {
                        var portada = fetchMultimedia.apply(dto.getPortadaId());
                        if (portada != null)
                                builder.portada(portada);
                }
                if (dto.getImagenContenido1Id() != null) {
                        var img1 = fetchMultimedia.apply(dto.getImagenContenido1Id());
                        if (img1 != null)
                                builder.imagenContenido1(img1);
                }
                if (dto.getImagenContenido2Id() != null) {
                        var img2 = fetchMultimedia.apply(dto.getImagenContenido2Id());
                        if (img2 != null)
                                builder.imagenContenido2(img2);
                }
                if (dto.getImagenContenido3Id() != null) {
                        var img3 = fetchMultimedia.apply(dto.getImagenContenido3Id());
                        if (img3 != null)
                                builder.imagenContenido3(img3);
                }

                if (dto.getId() != null) {
                        builder.id(dto.getId());
                }

                Post post = builder.build();

                // Limpia relaciones multimedia antiguas si es edición
                if (post.getPostMultimedia() != null) {
                        post.getPostMultimedia().clear();
                }

                // Solo usa multimediaIds para reconstruir la relación
                if (dto.getMultimediaIds() != null && !dto.getMultimediaIds().isEmpty()) {
                        dto.getMultimediaIds().forEach(multimediaId -> {
                                if (multimediaId == null)
                                        return;
                                com.example.demo.repository.entity.Multimedia multimedia = fetchMultimedia
                                                .apply(multimediaId);
                                if (multimedia == null)
                                        return;
                                PostMultimedia pm = new PostMultimedia();
                                PostMultimediaId pmId = new PostMultimediaId();
                                pmId.setPostId(post.getId());
                                pmId.setMultimediaId(multimedia.getId());
                                pm.setId(pmId);
                                pm.setPost(post);
                                pm.setMultimedia(multimedia);

                                String rol = null;
                                if (dto.getImagenes() != null) {
                                        var imgDto = dto.getImagenes().stream()
                                                        .filter(img -> {
                                                                try {
                                                                        // Busca por multimediaId si existe, si no por
                                                                        // id
                                                                        Long id = null;
                                                                        try {
                                                                                id = (Long) img.getClass().getMethod(
                                                                                                "getMultimediaId")
                                                                                                .invoke(img);
                                                                        } catch (Exception e) {
                                                                                id = (Long) img.getClass()
                                                                                                .getMethod("getId")
                                                                                                .invoke(img);
                                                                        }
                                                                        return id != null && id.equals(multimediaId);
                                                                } catch (Exception e) {
                                                                        return false;
                                                                }
                                                        })
                                                        .findFirst().orElse(null);
                                        if (imgDto != null) {
                                                try {
                                                        rol = (String) imgDto.getClass().getMethod("getRol")
                                                                        .invoke(imgDto);
                                                } catch (Exception ignored) {
                                                }
                                        }
                                }
                                pm.setRol(rol);

                                post.getPostMultimedia().add(pm);
                        });
                }

                System.out.println("PostMultimedia size: " + post.getPostMultimedia().size());
                for (PostMultimedia pm : post.getPostMultimedia()) {
                        System.out.println("PM id: " + pm.getId() + " multimedia: "
                                        + (pm.getMultimedia() != null ? pm.getMultimedia().getId() : "null"));
                }

                return post;
        }

        public static PostMultimediaDTO fromEntity(PostMultimedia pm) {
                return PostMultimediaDTO.builder()
                                .postId(pm.getPost().getId())
                                .multimediaId(pm.getMultimedia().getId())
                                .multimedia(MultimediaDTO.convertToDTO(pm.getMultimedia()))
                                .rol(pm.getRol())
                                .build();
        }
}