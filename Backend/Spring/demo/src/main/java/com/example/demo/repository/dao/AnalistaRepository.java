package com.example.demo.repository.dao;



import com.example.demo.repository.entity.Analista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalistaRepository extends JpaRepository<Analista, Long> {

    


}
