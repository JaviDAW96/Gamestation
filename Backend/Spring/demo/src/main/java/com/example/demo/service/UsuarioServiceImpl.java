package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UsuarioDTO registrar(UsuarioDTO usuarioDTO) {
        if (repo.existsByEmail(usuarioDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email ya registrado");
        }
        Usuario usuario = UsuarioDTO.convertToEntity(usuarioDTO);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario saved = repo.save(usuario);
        return UsuarioDTO.convertToDTO(saved);
    }

    @Override
    public List<UsuarioDTO> findAll() {
        return repo.findAll().stream()
            .map(UsuarioDTO::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO findById(Long id) {
        Usuario usuario = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + id));
        return UsuarioDTO.convertToDTO(usuario);
    }

    @Override
    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
        Usuario existente = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + id));
        existente.setNombre(usuarioDTO.getNombre());
        existente.setEmail(usuarioDTO.getEmail());
        // Opcional: actualizar rol u otros campos
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }
        Usuario updated = repo.save(existente);
        return UsuarioDTO.convertToDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + id);
        }
        repo.deleteById(id);
    }
}