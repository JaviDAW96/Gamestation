package com.example.demo.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.repository.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTipo(String tipo);

    List<Post> findByUsuarioId(Long usuarioId);

    @Query("""
                SELECT p FROM Post p
                LEFT JOIN FETCH p.postMultimedia
                LEFT JOIN FETCH p.comentarios
                LEFT JOIN FETCH p.reacciones
                LEFT JOIN FETCH p.etiquetas
                LEFT JOIN FETCH p.categorias
                WHERE p.tipo = :tipo
            """)
    List<Post> findAllByTipoWithAllRelations(@Param("tipo") String tipo);

    // Búsqueda por texto en varios campos y opcionalmente por tipo
    @Query("SELECT p FROM Post p WHERE " +
            "(:tipo IS NULL OR LOWER(p.tipo) = LOWER(:tipo)) AND " +
            "(LOWER(p.titulo) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.subtitulo) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.contenido) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Post> searchPosts(@Param("q") String q, @Param("tipo") String tipo);
}
