package br.edu.ifpb.dac.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;

public record AdvertisementResponseDTO(
    Long id,
    AdvertisementOwnerDTO advertiser,
    List<ProductDTO> products,
    String description,
    @PositiveOrZero
    BigDecimal totalPrice,
    Date createdAt
) {

}
