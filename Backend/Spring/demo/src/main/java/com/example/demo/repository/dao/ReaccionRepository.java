package com.example.demo.repository.dao;

import com.example.demo.repository.entity.Reaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaccionRepository extends JpaRepository<Reaccion, Long> {
    long countByPostIdAndTipo(Long postId, String tipo); // Cuenta las reacciones por tipo (LIKE o DISLIKE)
}