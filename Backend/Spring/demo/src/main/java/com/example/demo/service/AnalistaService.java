package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.AnalistaDTO;


public interface AnalistaService {
   

    List<AnalistaDTO> findAll();

    /**
     * Obtiene un usuario por ID y devuelve su DTO
     */
    AnalistaDTO findById(Long id);

    AnalistaDTO findByUsuarioId(Long idUsuario);

    /**
     * Actualiza un usuario existente
     */
    AnalistaDTO update(Long id, AnalistaDTO analistaDTO);

    /**
     * Elimina un usuario por ID
     */
    void delete(Long id);
}
