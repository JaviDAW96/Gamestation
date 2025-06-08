package com.example.demo.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.example.demo.model.dto.UsuarioDTO;


@Service
public interface UsuarioService  {

        /**
     * Registra un nuevo usuario y devuelve su DTO
     */
    UsuarioDTO registrar(UsuarioDTO usuarioDTO);

    /**
     * Obtiene todos los usuarios
     */
    List<UsuarioDTO> findAll();

    /**
     * Obtiene un usuario por ID y devuelve su DTO
     */
    UsuarioDTO findById(Long id);

    /**
     * Actualiza un usuario existente
     */
    UsuarioDTO update(Long id, UsuarioDTO usuarioDTO);

    /**
     * Elimina un usuario por ID
     */
    void delete(Long id);
}
