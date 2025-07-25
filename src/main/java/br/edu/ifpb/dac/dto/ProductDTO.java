package br.edu.ifpb.dac.dto;

import br.edu.ifpb.dac.entity.Category;
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
    private Category category;
    private Long userId;
}