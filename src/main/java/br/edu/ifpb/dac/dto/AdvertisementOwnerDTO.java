package br.edu.ifpb.dac.dto;

public record AdvertisementOwnerDTO(
        Long id,
        String username,
        String imgUrl,
        String fullName,
        String enrollmentNumber
) { }
