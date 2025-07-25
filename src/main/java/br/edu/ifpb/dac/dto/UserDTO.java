package br.edu.ifpb.dac.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@ifpb\\.edu\\.br$",
                message = "email must end with @ifpb.edu.br"
        )
        String username,
        @NotBlank @Size(min=8, max=30) String password,
        @NotBlank String fullName,
        @NotBlank String enrollmentNumber
) { }
