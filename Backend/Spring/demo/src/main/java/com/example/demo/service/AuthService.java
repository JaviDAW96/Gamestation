package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.AuthResponseDTO;
import com.example.demo.model.dto.LoginDTO;
import com.example.demo.model.dto.RegistroDTO;
import com.example.demo.model.dto.RolDTO;
import com.example.demo.model.dto.UsuarioDTO;
import com.example.demo.repository.dao.RolRepository;
import com.example.demo.repository.dao.UsuarioRepository;
import com.example.demo.repository.entity.Rol;
import com.example.demo.repository.entity.Usuario;
import com.example.demo.utils.JwtUtil;

/**
 * Servicio que gestiona el registro y login de usuarios.
 * Aquí se hace la lógica principal de autenticación.
 */
@Service
public class AuthService {
    // Permite autenticar usuarios (login)
    @Autowired private AuthenticationManager authManager;
    // Utilidad para crear y validar tokens JWT
    @Autowired private JwtUtil jwtUtil;
    // Acceso a los usuarios en la base de datos
    @Autowired private UsuarioRepository userRepo;
    // Acceso a los roles en la base de datos
    @Autowired private RolRepository roleRepo;
    // Para encriptar y comprobar contraseñas
    @Autowired private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en la aplicación.
     * - Comprueba que el nombre y el email no estén ya usados.
     * - Crea el usuario, le asigna el rol "USER" y guarda todo en la base de datos.
     * - Genera un token JWT para el usuario registrado.
     * - Devuelve los datos del usuario y el token.
     */
    public AuthResponseDTO registro(RegistroDTO dto) {
        // Si el nombre ya existe, lanza error
        if (userRepo.existsByNombre(dto.getNombre()))
            throw new RuntimeException("El nombre de usuario ya existe");
        // Si el email ya existe, lanza error
        if (userRepo.existsByEmail(dto.getEmail()))
            throw new RuntimeException("El email ya está registrado");

        // Crea el usuario y le pone los datos del DTO
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        // Encripta la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Determina el rol a asignar (por defecto "usuario")

        // Busca el rol "usuario" en la base de datos
        Rol rolUser = roleRepo.findByRol("usuario")
                                .orElseThrow(() -> new RuntimeException("Rol usuario no encontrado"));
        usuario.setRol(rolUser);
        // Guarda el usuario en la base de datos
        userRepo.save(usuario);

        // Genera un token JWT usando el email del usuario
        String token = jwtUtil.generateToken(usuario.getEmail());
        // Convierte el usuario y el rol a DTOs para devolverlos
        UsuarioDTO userDto = UsuarioDTO.convertToDTO(usuario);
        RolDTO rolDto = RolDTO.convertToDTO(rolUser);
        // Devuelve el token, el usuario y el rol en un objeto de respuesta
        return AuthResponseDTO.builder()
                .token(token)
                .usuario(userDto)
                .rol(rolDto)
                .build();
    }

    /**
     * Autentica (login) a un usuario.
     * - Comprueba el nombre y la contraseña.
     * - Si son correctos, genera un token JWT.
     * - Devuelve los datos del usuario y el token.
     */
    public AuthResponseDTO login(LoginDTO dto) {
        // Primero intenta buscar el usuario por email (ya que el email es único)
        Usuario usuario = userRepo.findByEmail(dto.getNombre())
            .orElseGet(() -> {
                // Si no lo encuentra por email, busca por nombre de usuario
                // Puede haber varios usuarios con el mismo nombre, así que obtenemos una lista
                List<Usuario> usuarios = userRepo.findAllByNombre(dto.getNombre());
                if (usuarios.size() == 1) {
                    // Si solo hay uno, lo usamos
                    return usuarios.get(0);
                } else if (usuarios.size() > 1) {
                    // Si hay más de uno, lanzamos un error para que el usuario use su email
                    throw new RuntimeException("Hay más de un usuario con ese nombre. Usa tu email para iniciar sesión.");
                } else {
                    // Si no hay ninguno, devolvemos null
                    return null;
                }
            });

        // Si no se encontró ningún usuario, lanzamos un error
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Autenticamos al usuario usando su email y la contraseña que ha introducido
        // Spring Security comprobará que la contraseña es correcta (comparando el hash)
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(usuario.getEmail(), dto.getPassword())
        );

        // Obtenemos los detalles del usuario autenticado
        UserDetails ud = (UserDetails) auth.getPrincipal();
        // Generamos un token JWT para este usuario
        String token = jwtUtil.generateToken(ud.getUsername());

        // Convertimos el usuario y el rol a DTOs para devolverlos al frontend
        UsuarioDTO userDto = UsuarioDTO.convertToDTO(usuario);
        RolDTO rolDto = RolDTO.convertToDTO(usuario.getRol());

        // Devolvemos el token, el usuario y el rol en la respuesta
        return AuthResponseDTO.builder()
                .token(token)
                .usuario(userDto)
                .rol(rolDto)
                .nombre(usuario.getNombre()) // <-- Añade esto
                .build();
    }
}
