package com.example.demo.service;



import com.example.demo.model.dto.AnalistaDTO;
import com.example.demo.repository.dao.AnalistaRepository;
import com.example.demo.repository.entity.Analista;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalistaServiceImpl implements AnalistaService {

    private final AnalistaRepository repository;

    /** Crear nuevo analista */
    public AnalistaDTO create(AnalistaDTO dto) {
        Analista entity = AnalistaDTO.convertToEntity(dto);
        Analista saved = repository.save(entity);
        return AnalistaDTO.convertToDTO(saved);
    }

    /** Obtener todos los analistas */
    public List<AnalistaDTO> findAll() {
        return repository.findAll().stream()
                .map(AnalistaDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    /** Obtener uno por ID */
    public AnalistaDTO findById(Long id) {
        Analista analista = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analista no encontrado con id " + id));
        return AnalistaDTO.convertToDTO(analista);
    }

    /** Actualizar un analista existente */
    public AnalistaDTO update(Long id, AnalistaDTO dto) {

        // Primero obtenemos el analista a actualizar
        Analista analista = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analista no encontrado con id " + id));
        
        // Segundo generamos el nuevo analista que actualizaremos
        // y lo guardamos en la base de datos
        // (no es necesario volver a buscarlo)
        Analista toSave = Analista.builder()
                .id(analista.getId())
                .descripcion(dto.getDescripcion())
                .experienciaLaboral(dto.getExperienciaLaboral())
                .noticiasPublicadas(dto.getNoticiasPublicadas())
                .build();

        // Una vez construido con builder, lo guardamos
        // y lo convertimos a DTO para devolverlo
        Analista saved = repository.save(toSave);
        return AnalistaDTO.convertToDTO(saved);
    }

    /** Borrar un analista */
    public void delete(Long id) {
        repository.deleteById(id);
    }

}
