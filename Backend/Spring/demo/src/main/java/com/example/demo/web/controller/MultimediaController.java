package com.example.demo.web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.dto.MultimediaDTO;
import com.example.demo.repository.entity.Multimedia;
import com.example.demo.service.MultimediaService;

import java.util.Map;

@RestController
@RequestMapping("/api/imagenes")
public class MultimediaController {

    @Autowired
    private MultimediaService multimediaService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        throw new UnsupportedOperationException("Implementa la lógica de subida aquí");
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody MultimediaDTO dto) {
        Multimedia multimedia = multimediaService.create(dto);

        Map<String, Object> response = Map.of(
            "id", multimedia.getId(),
            "url", multimedia.getUrl()
        );
        
        return ResponseEntity.ok(response);
    }
}
