package br.edu.ifpb.dac.dto;

public record UserResponseDTO(
        Long id,
        String username,
        String imgUrl,
        String fullName,
        String enrollmentNumber
) { }
