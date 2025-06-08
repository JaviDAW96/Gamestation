package com.example.demo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    // Clave secreta para firmar y verificar los tokens JWT 
    private static final String SECRET = "MiClaveSecretaSuperSeguraYDeAlMenos256Bits";

    // Objeto clave generado a partir de la clave secreta
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Tiempo de expiración del token en milisegundos (24 horas)
    private final long expirationMs = 86400000;

    /**
     * Genera un token JWT para un usuario dado.
     * @param nombre El nombre de usuario (o email) que irá en el token.
     * @return El token JWT generado como String.
     */
    public String generateToken(String nombre) {
        return Jwts.builder()
                .subject(nombre) // El usuario será el "subject" del token
                .issuedAt(new Date()) // Fecha de emisión
                .expiration(new Date(System.currentTimeMillis() + expirationMs)) // Fecha de expiración
                .signWith(key, Jwts.SIG.HS256) // Firma el token con la clave y el algoritmo HS256
                .compact(); // Genera el token como String
    }

    /**
     * Extrae el nombre de usuario (subject) de un token JWT.
     * @param token El token JWT recibido.
     * @return El nombre de usuario que está en el token.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key) // Usa la clave para verificar la firma
                .build()
                .parseSignedClaims(token) // Parsea y valida el token
                .getPayload() // Obtiene los datos del token (datos del usuario)
                .getSubject(); // Obtiene el "subject" (usuario)
    }

    /**
     * Valida si un token JWT es correcto y no ha expirado.
     * @param token El token JWT a validar.
     * @return true si el token es válido, false si es inválido o ha expirado.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token); // Intenta validar el token
            return true; // Si no hay error, el token es válido
        } catch (Exception e) {
            return false; // Si hay cualquier error, el token no es válido
        }
    }
}