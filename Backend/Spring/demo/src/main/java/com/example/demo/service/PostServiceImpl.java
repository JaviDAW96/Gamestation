package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.PostDTO;

import com.example.demo.repository.dao.CategoriaRepository;
import com.example.demo.repository.dao.EtiquetaRepository;
import com.example.demo.repository.dao.PostRepository;
import com.example.demo.repository.dao.ReaccionRepository;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.dao.MultimediaRepository;
import com.example.demo.repository.dao.AnalistaRepository;

import com.example.demo.repository.entity.Categoria;
import com.example.demo.repository.entity.Etiqueta;
import com.example.demo.repository.entity.Post;
import com.example.demo.repository.entity.PostCategoria;
import com.example.demo.repository.entity.PostCategoriaId;
import com.example.demo.repository.entity.PostEtiqueta;
import com.example.demo.repository.entity.PostEtiquetaId;
import com.example.demo.repository.entity.Reaccion;
import com.example.demo.repository.entity.PostMultimedia;
import com.example.demo.repository.entity.PostMultimediaId;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepo;
    @Autowired
    private ReaccionRepository reaccionRepo;
    @Autowired
    private EtiquetaRepository etiquetaRepo;
    @Autowired
    private CategoriaRepository categoriaRepo;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MultimediaRepository multimediaRepo;
    @Autowired
    private AnalistaRepository analistaRepository;

    @Override
    public List<PostDTO> findAll() {
        return postRepo.findAll().stream()
                .map(PostDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO create(PostDTO dto) {
        Post entity = PostDTO.convertToEntity(
                dto,
                usuarioId -> usuarioRepository.findById(usuarioId).orElse(null),
                categoriaId -> categoriaRepo.findById(categoriaId).orElse(null),
                etiquetaId -> etiquetaRepo.findById(etiquetaId).orElse(null),
                multimediaId -> multimediaRepo.findById(multimediaId).orElse(null),
                postId -> postRepo.findById(postId).orElse(null) // <-- aquí
        );

        // Guardamos
        Post guardado = postRepo.save(entity);

        // Si el post es de tipo noticia, actualiza el contador en analista
        if ("noticia".equalsIgnoreCase(entity.getTipo())) {
            analistaRepository.findByIdUsuario(entity.getUsuario().getId()).ifPresent(analista -> {
                int nuevasNoticias = (analista.getNoticiasPublicadas() == null ? 0 : analista.getNoticiasPublicadas()) + 1;
                analista.setNoticiasPublicadas(nuevasNoticias);
                analistaRepository.save(analista);
            });
        }

        return PostDTO.convertToDTO(guardado);
    }

    @Override
    public PostDTO update(Long id, PostDTO dto) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado: " + id));

        // — Campos simples —
        post.setTitulo(dto.getTitulo());
        post.setSubtitulo(dto.getSubtitulo());
        post.setDescripcion(dto.getDescripcion());
        post.setContenido(dto.getContenido());
        post.setTipo(dto.getTipo());
        post.setFechaPublicacion(dto.getFechaPublicacion());

        // — Categorías —
        
        post.getCategorias().clear();
        if (dto.getCategorias() != null) {
            dto.getCategorias().forEach(catDto -> {
                Categoria cat = categoriaRepo.findById(catDto.getIdCategoria())
                        .orElseThrow(() -> new RuntimeException("Categoria no encontrada: " + catDto.getIdCategoria()));
                PostCategoria pc = new PostCategoria(
                        new PostCategoriaId(post.getId(), cat.getId()),
                        post,
                        cat);
                post.getCategorias().add(pc);
            });
        }

        // — Etiquetas —
        post.getEtiquetas().clear();
        if (dto.getEtiquetas() != null) {
            dto.getEtiquetas().forEach(tagDto -> {
                Etiqueta tag = etiquetaRepo.findById(tagDto.getIdEtiqueta())
                        .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada: " + tagDto.getIdEtiqueta()));
                PostEtiqueta pe = new PostEtiqueta(
                        new PostEtiquetaId(post.getId(), tag.getId()),
                        post,
                        tag);
                post.getEtiquetas().add(pe);
            });
        }

        // — Multimedia —
        post.getPostMultimedia().clear();
        if (dto.getMultimediaIds() != null) {
            dto.getMultimediaIds().forEach(multimediaId -> {
                var multimedia = multimediaRepo.findById(multimediaId)
                    .orElseThrow(() -> new RuntimeException("Multimedia no encontrada: " + multimediaId));
                var pm = new PostMultimedia();
                var pmId = new PostMultimediaId();
                pmId.setPostId(post.getId());
                pmId.setMultimediaId(multimedia.getId());
                pm.setId(pmId);
                pm.setPost(post);
                pm.setMultimedia(multimedia);

                // Busca el rol en el array de imágenes del DTO
                String rol = null;
                if (dto.getImagenes() != null) {
                    var imgDto = dto.getImagenes().stream()
                        .filter(img -> img.getMultimediaId() != null && img.getMultimediaId().equals(multimediaId))
                        .findFirst().orElse(null);
                    if (imgDto != null) {
                        rol = imgDto.getRol();
                    }
                }
                pm.setRol(rol);

                post.getPostMultimedia().add(pm);
            });
        }

        // Hibernate gestionará las inserciones/borrados
        Post actualizado = postRepo.save(post);
        return PostDTO.convertToDTO(actualizado);
    }

    @Override
    public PostDTO findById(Long id) {
        Post p = postRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado: " + id));
        return PostDTO.convertToDTO(p);
    }

    public Post save(Post post) {
        return postRepo.save(post);
    }

    public void delete(Long id) {
        postRepo.deleteById(id);

    }

    public void incrementLikes(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        Reaccion reaccion = new Reaccion();
        reaccion.setPost(post);
        reaccion.setTipo("LIKE");

        reaccionRepo.save(reaccion);
    }

    public long countLikes(Long postId) {
        return reaccionRepo.countByPostIdAndTipo(postId, "LIKE");
    }

    public long countDislikes(Long postId) {
        return reaccionRepo.countByPostIdAndTipo(postId, "DISLIKE");
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDTO> findAllByTipo(String tipo) {
        List<Post> posts = postRepo.findAllByTipoWithAllRelations(tipo);
        System.out.println("Posts recuperados: " + posts.size());
        for (Post p : posts) {
            System.out.println("Post ID: " + p.getId());
            System.out.println("PostMultimedia: " + (p.getPostMultimedia() != null ? p.getPostMultimedia().size() : "null"));
            System.out.println("Comentarios: " + (p.getComentarios() != null ? p.getComentarios().size() : "null"));
            System.out.println("Reacciones: " + (p.getReacciones() != null ? p.getReacciones().size() : "null"));
            System.out.println("Etiquetas: " + (p.getEtiquetas() != null ? p.getEtiquetas().size() : "null"));
            System.out.println("Categorias: " + (p.getCategorias() != null ? p.getCategorias().size() : "null"));
        }
        return posts.stream().map(PostDTO::convertToDTO).toList();
    }

    @Override
    public List<PostDTO> search(String q, String tipo) {
        List<Post> posts = postRepo.searchPosts(q, tipo);
        return posts.stream().map(PostDTO::convertToDTO).collect(Collectors.toList());
    }


}