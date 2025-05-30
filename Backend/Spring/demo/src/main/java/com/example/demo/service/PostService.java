package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.PostDTO;

public interface PostService {
    List<PostDTO> findAll();
    PostDTO findById(Long id);
    PostDTO create(PostDTO dto);
    PostDTO update(Long id, PostDTO dto);
    void delete(Long id);
    void incrementLikes(Long id);
    List<PostDTO> findAllByTipo(String tipo);
}
