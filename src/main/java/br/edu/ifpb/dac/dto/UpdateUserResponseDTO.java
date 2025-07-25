package br.edu.ifpb.dac.dto;

public record UpdateUserResponseDTO(
        Long id,
        String username,
        String fullName,
        String enrollmentNumber
) { }
