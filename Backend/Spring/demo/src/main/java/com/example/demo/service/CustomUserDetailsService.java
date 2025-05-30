package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Usuario;

/**
 * Servicio que le dice a Spring Security cómo cargar los datos de un usuario
 * cuando alguien intenta iniciar sesión.
 * 
 * Permite que el usuario pueda iniciar sesión usando su nombre de usuario o su email.
 * Internamente, siempre se usará el email como identificador único para Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String nombreOEmail) 
            throws UsernameNotFoundException {
        // Busca primero por email, si no encuentra, busca por nombre de usuario.
        // Así, el usuario puede escribir cualquiera de los dos en el login.
        Usuario usuario = userRepo.findByEmail(nombreOEmail)
            .orElseGet(() -> userRepo.findAllByNombre(nombreOEmail).stream().findFirst().orElse(null));

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + nombreOEmail);
        }

        // Aquí, aunque el usuario haya escrito su nombre, usamos el email como "username" interno.
        // Esto es porque el email es único y así evitamos confusiones o duplicados.
        // Spring Security usará este email como identificador en todo el sistema.
        return org.springframework.security.core.userdetails.User
            .withUsername(usuario.getEmail()) // El identificador interno será siempre el email
            .password(usuario.getPassword())  // Contraseña cifrada
            .authorities(usuario.getRol().getRol()) // El rol del usuario, por ejemplo "ROLE_USER"
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
}
