package br.edu.ifpb.dac.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private boolean forExchange;
    private int quantity;

    private Long categoryId;

    private Long userId;
    private String imageUrl;
}