package com.example.demo.service;

import com.example.demo.model.dto.MultimediaDTO;
import com.example.demo.repository.entity.Multimedia;

public interface MultimediaService {
    Multimedia create(MultimediaDTO dto);
}
