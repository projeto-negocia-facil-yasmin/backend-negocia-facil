package br.edu.ifpb.dac.dto;

public record AuthRequest(
        String username,
        String password,
        String fullName,
        String phone,
        String imgUrl,
        String enrollmentNumber
) {}