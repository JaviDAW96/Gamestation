package com.example.demo.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.PostDTO;
import com.example.demo.service.PostServiceImpl;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostServiceImpl postService;

    /**
     * Devuelve todos los posts en formato DTO
     */

    @GetMapping
    public List<PostDTO> listAllByTipo(@RequestParam String tipo) {
        return postService.findAllByTipo(tipo);
    }

    /**
     * Crea un nuevo post (rol ANALYST)
     */
    @PostMapping
    @PreAuthorize("hasRole('analista')")
    public PostDTO createPost(@RequestBody PostDTO dto) {
        dto.setId(null); // Fuerza que el id sea null al crear
        return postService.create(dto);
    }

    /**
     * Actualiza un post existente por ID (rol ANALYST)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('analista')")
    public PostDTO updatePost(@PathVariable Long id, @RequestBody PostDTO dto) {
        return postService.update(id, dto);
    }

    /**
     * Elimina un post por ID (rol ANALYST)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('analista')")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Incrementa likes para un post (rol USER)
     */
    @PostMapping("/{id}/like")
    @PreAuthorize("hasRole('usuario')")
    public ResponseEntity<?> likePost(@PathVariable Long id) {
        postService.incrementLikes(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Busca posts por query y tipo (opcional)
     */
    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> search(
            @RequestParam("q") String q,
            @RequestParam(value = "tipo", required = false) String tipo) {
        return ResponseEntity.ok(postService.search(q, tipo));
    }

    /**
     * Obtiene un post por su ID
     */
    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable Long id) {
        return postService.findById(id);
    }
}
