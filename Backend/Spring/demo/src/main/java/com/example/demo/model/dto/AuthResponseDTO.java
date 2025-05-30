package com.example.demo.model.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String token;
    private UsuarioDTO usuario;
    private RolDTO rol;
    private List<String> roles;
    private String nombre; // <-- Añade esto
}
