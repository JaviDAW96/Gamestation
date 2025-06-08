package com.example.demo.repository.dao;



import com.example.demo.repository.entity.Analista;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AnalistaRepository extends JpaRepository<Analista, Long> {
    Optional<Analista> findByIdUsuario(Long idUsuario);
}