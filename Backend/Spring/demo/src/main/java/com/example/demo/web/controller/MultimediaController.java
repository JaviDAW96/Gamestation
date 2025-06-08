package com.example.demo.web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.dto.MultimediaDTO;
import com.example.demo.repository.entity.Multimedia;
import com.example.demo.service.MultimediaService;

@RestController
@RequestMapping("/api/multimedia")
public class MultimediaController {

    @Autowired
    private MultimediaService multimediaService;

    @PostMapping
    public ResponseEntity<MultimediaDTO> create(@RequestBody MultimediaDTO dto) {
        Multimedia multimedia = multimediaService.create(dto);
        return ResponseEntity.ok(MultimediaDTO.convertToDTO(multimedia));
    }
}
