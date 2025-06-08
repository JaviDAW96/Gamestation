package com.example.demo.service;



import com.example.demo.model.dto.AnalistaDTO;
import com.example.demo.model.dto.PostDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.dao.AnalistaRepository;
import com.example.demo.repository.dao.PostRepository;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Analista;
import com.example.demo.repository.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalistaServiceImpl implements AnalistaService {

    private final AnalistaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository; // o el servicio correspondiente

    @Autowired
    private PostRepository postRepository; // o el servicio correspondiente

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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Analista no encontrado con id " + id));
        Usuario usuario = usuarioRepository.findById(analista.getIdUsuario())
            .orElse(null);
        UsuarioDTO usuarioDTO = UsuarioDTO.convertToDTO(usuario);
        List<PostDTO> posts = postRepository.findByUsuarioId(analista.getIdUsuario())
            .stream().map(PostDTO::convertToDTO).collect(Collectors.toList());
        return AnalistaDTO.convertToDTO(analista, usuarioDTO, posts);
    }

    /** Actualizar un analista existente */
    public AnalistaDTO update(Long id, AnalistaDTO dto) {
        // 1. Buscar el analista existente
        Analista analista = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analista no encontrado con id " + id));

        // 2. Buscar el usuario asociado
        Usuario usuario = usuarioRepository.findById(analista.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + analista.getIdUsuario()));

        // 3. Actualizar campos del analista
        analista.setDescripcion(dto.getDescripcion());
        analista.setExperienciaLaboral(dto.getExperienciaLaboral());
        analista.setNoticiasPublicadas(dto.getNoticiasPublicadas());

        // 4. Actualizar campos del usuario (si vienen en el DTO)
        if (dto.getUsuario() != null) {
            UsuarioDTO usuarioDTO = dto.getUsuario();
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setApellidos(usuarioDTO.getApellidos());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setImagen(usuarioDTO.getImagen());
            // Actualiza otros campos si es necesario
            usuarioRepository.save(usuario);
        }

        // 5. Guardar el analista actualizado
        Analista saved = repository.save(analista);

        // 6. Devolver el DTO actualizado
        UsuarioDTO usuarioDTO = UsuarioDTO.convertToDTO(usuario);
        List<PostDTO> posts = postRepository.findByUsuarioId(usuario.getId())
            .stream().map(PostDTO::convertToDTO).collect(Collectors.toList());
        return AnalistaDTO.convertToDTO(saved, usuarioDTO, posts);
    }

    /** Borrar un analista */
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public AnalistaDTO findByUsuarioId(Long idUsuario) {
    Analista analista = repository.findByIdUsuario(idUsuario)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Analista no encontrado para usuario " + idUsuario));
    return AnalistaDTO.convertToDTO(analista);
}

}
