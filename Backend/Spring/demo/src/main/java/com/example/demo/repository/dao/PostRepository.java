package com.example.demo.repository.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.repository.entity.Post;



@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

    List<Post> findByTipo(String tipo);
}
