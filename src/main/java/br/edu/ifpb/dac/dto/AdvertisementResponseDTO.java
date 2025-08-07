package br.edu.ifpb.dac.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;

public record AdvertisementResponseDTO(
    Long id,
    AdvertisementOwnerDTO advertiser,
    List<ProductDTO> products,
    @PositiveOrZero
    BigDecimal totalPrice,
    LocalDateTime createdAt
) {
}