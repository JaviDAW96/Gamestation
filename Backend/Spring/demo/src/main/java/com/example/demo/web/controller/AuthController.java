package com.example.demo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.AuthResponseDTO;
import com.example.demo.model.dto.LoginDTO;
import com.example.demo.model.dto.RegistroDTO;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;
import java.util.Map;

/**
 * Controlador REST para autenticación y registro de usuarios.
 * Expone endpoints para registro y login.
 * Utiliza AuthService para la lógica de negocio.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    /**
     * Endpoint para registrar un nuevo usuario.
     * @param dto Datos del usuario a registrar.
     * @return ResponseEntity con el resultado del registro o error.
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody @Valid RegistroDTO dto) {
        try {
            AuthResponseDTO resp = authService.registro(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (RuntimeException ex) {
            // Errores de negocio (usuario ya existe, email duplicado, etc.)
            log.warn("Error en registro de usuario: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            // Errores inesperados
            log.error("Error interno en registro de usuario", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Endpoint para login de usuario.
     * @param dto Datos de login (usuario/email y contraseña).
     * @return ResponseEntity con el resultado del login o error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO dto) {
        try {
            AuthResponseDTO resp = authService.login(dto);
            return ResponseEntity.ok(resp);
        } catch (RuntimeException ex) {
            // Errores de autenticación o negocio
            log.warn("Error en login: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            // Errores inesperados
            log.error("Error interno en login", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Endpoint para logout de usuario.
     * @return ResponseEntity con el resultado del logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Aquí podrías invalidar el token si tuvieras una lista negra
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada correctamente"));
    }
}
