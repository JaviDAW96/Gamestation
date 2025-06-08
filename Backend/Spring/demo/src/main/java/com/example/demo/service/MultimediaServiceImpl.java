package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.MultimediaDTO;
import com.example.demo.repository.dao.MultimediaRepository;
import com.example.demo.repository.entity.Multimedia;

@Service
@Transactional
public class MultimediaServiceImpl implements MultimediaService {

    @Autowired
    private MultimediaRepository multimediaRepo;

    @Override
    public Multimedia create(MultimediaDTO dto) {
        Multimedia multimedia;
        if (dto.getId() != null) {
            multimedia = multimediaRepo.findById(dto.getId()).orElseGet(() -> MultimediaDTO.convertToEntity(dto));
            // Actualiza campos si ya existe
            multimedia.setUrl(dto.getUrl());
            multimedia.setTipoContenido(dto.getTipoContenido());
            multimedia.setFechaSubida(dto.getFechaSubida());
            multimedia.setNombre(dto.getNombre());
            multimedia.setDescripcion(dto.getDescripcion());
        } else {
            multimedia = MultimediaDTO.convertToEntity(dto);
        }
        return multimediaRepo.save(multimedia);
    }
}
