package com.example.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
}
