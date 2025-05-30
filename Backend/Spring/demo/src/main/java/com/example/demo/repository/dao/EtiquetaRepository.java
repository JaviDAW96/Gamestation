package com.example.demo.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.repository.entity.Etiqueta;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {
}