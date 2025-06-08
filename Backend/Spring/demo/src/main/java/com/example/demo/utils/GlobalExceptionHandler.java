package com.example.demo.utils;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Esta clase se encarga de manejar los errores (excepciones) que ocurren en los controladores REST de la aplicación.
 * Así, puedes devolver mensajes de error claros y personalizados al frontend.
 */
@RestControllerAdvice // Indica que esta clase va a manejar excepciones de todos los controladores REST
public class GlobalExceptionHandler {

    /**
     * Este método se ejecuta automáticamente cuando ocurre un error de validación en los datos recibidos (por ejemplo, un DTO con campos inválidos).
     * Captura la excepción MethodArgumentNotValidException y construye un mapa con los nombres de los campos y sus mensajes de error.
     * Devuelve ese mapa como respuesta con código 400 (Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        // Recorre todos los errores de validación y los pone en un mapa: campo -> mensaje de error
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(f -> f.getField(), FieldError::getDefaultMessage));
        // Devuelve el mapa de errores al frontend with estado 400
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Este método captura cualquier otra excepción que no haya sido manejada antes.
     * Devuelve un mensaje genérico de error y el código 500 (Error interno del servidor).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        ex.printStackTrace(); // <-- Añade esta línea para ver el error real en consola
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + ex.getMessage());
    }
}