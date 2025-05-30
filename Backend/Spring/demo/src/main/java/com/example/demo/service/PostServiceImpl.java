package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.model.dto.PostDTO;

import com.example.demo.repository.dao.CategoriaRepository;
import com.example.demo.repository.dao.EtiquetaRepository;
import com.example.demo.repository.dao.PostRepository;
import com.example.demo.repository.dao.ReaccionRepository;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Categoria;
import com.example.demo.repository.entity.Etiqueta;
import com.example.demo.repository.entity.Post;
import com.example.demo.repository.entity.PostCategoria;
import com.example.demo.repository.entity.PostCategoriaId;
import com.example.demo.repository.entity.PostEtiqueta;
import com.example.demo.repository.entity.PostEtiquetaId;
import com.example.demo.repository.entity.Reaccion;

import jakarta.transaction.Transactional;
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

    @Override
    public List<PostDTO> findAll() {
        return postRepo.findAll().stream()
                .map(PostDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO create(PostDTO dto) {
        // Convertimos PostDTO a Post, inyectando cómo obtener cada entidad
        Post entity = PostDTO.convertToEntity(
                dto,
                usuarioId -> usuarioRepository.findById(usuarioId)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId)),
                categoriaId -> categoriaRepo.findById(categoriaId)
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + categoriaId)),
                etiquetaId -> etiquetaRepo.findById(etiquetaId)
                        .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada: " + etiquetaId)),
                multimediaId -> null);

        // Guardamos
        Post guardado = postRepo.save(entity);
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
        // Limpio y dejo que orphanRemoval haga el borrado
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
    public List<PostDTO> findAllByTipo(String tipo) {
        return postRepo.findByTipo(tipo).stream()
                .map(PostDTO::convertToDTO)
                .collect(Collectors.toList());
    }


}