package br.edu.ifpb.dac.mapper;

import br.edu.ifpb.dac.dto.ProductDTO;
import br.edu.ifpb.dac.entity.Product;
import br.edu.ifpb.dac.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDTO dto, User user) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(dto.getCategory());
        product.setForExchange(dto.isForExchange());
        product.setUser(user);
        product.setImageUrl(dto.getImageUrl());
        return product;
    }

    public ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCategory(product.getCategory());
        dto.setForExchange(product.isForExchange());
        dto.setUserId(product.getUser().getId());
        dto.setImageUrl(product.getImageUrl());
        return dto;
    }
}