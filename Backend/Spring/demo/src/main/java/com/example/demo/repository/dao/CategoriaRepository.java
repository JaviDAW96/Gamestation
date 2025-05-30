package com.example.demo.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.repository.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

