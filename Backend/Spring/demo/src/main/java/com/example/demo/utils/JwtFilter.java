package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro que se ejecuta en cada petición HTTP para comprobar si hay un token JWT válido.
 * Si el token es válido, autentica al usuario en el contexto de Spring Security.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    // Utilidad para trabajar con tokens JWT (validar, extraer usuario, etc.)
    @Autowired private JwtUtil jwtUtil;
    // Servicio para cargar los datos del usuario desde la base de datos
    @Autowired private CustomUserDetailsService uds;

    /**
     * Este método se ejecuta automáticamente en cada petición HTTP.
     * Se encarga de comprobar si la petición lleva un token JWT válido y, si es así,
     * autentica al usuario en el sistema de seguridad de Spring.
     */
    @Override
    protected void doFilterInternal(
            @org.springframework.lang.NonNull HttpServletRequest req,
            @org.springframework.lang.NonNull HttpServletResponse res,
            @org.springframework.lang.NonNull FilterChain chain)
            throws ServletException, IOException {
        // Obtiene el valor de la cabecera "Authorization" de la petición
        String header = req.getHeader("Authorization");
        // Comprueba que la cabecera existe y empieza por "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            // Extrae el token JWT quitando el prefijo "Bearer "
            String token = header.substring(7);
            // Valida el token usando JwtUtil
            if (jwtUtil.validateToken(token)) {
                // Extrae el nombre de usuario (o email) del token
                String username = jwtUtil.extractUsername(token);
                // Carga los detalles del usuario desde la base de datos
                UserDetails ud = uds.loadUserByUsername(username);

                // Crea un objeto de autenticación con los datos del usuario y sus roles
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                // Registra la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continúa con el siguiente filtro o controlador
        chain.doFilter(req, res);
    }
}
