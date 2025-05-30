package com.example.demo.web.controller;


import com.example.demo.model.dto.AnalistaDTO;
import com.example.demo.service.AnalistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analistas")
@RequiredArgsConstructor
public class AnalistaController {

    private final AnalistaService service;


    /** Leer todos */
    @GetMapping
    public ResponseEntity<List<AnalistaDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /** Leer uno */
    @GetMapping("/{id}")
    public ResponseEntity<AnalistaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /** Actualizar */
    @PutMapping("/{id}")
    public ResponseEntity<AnalistaDTO> update(@PathVariable Long id, @RequestBody AnalistaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /** Borrar */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
