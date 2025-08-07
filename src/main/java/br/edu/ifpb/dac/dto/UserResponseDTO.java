package br.edu.ifpb.dac.dto;

import java.util.List;

public record UserResponseDTO(
        Long id,
        String username,
        String imgUrl,
        String fullName,
        String enrollmentNumber,
        List<String> roles
) { }
