package br.edu.ifpb.dac.service;

import br.edu.ifpb.dac.dto.ProductDTO;
import br.edu.ifpb.dac.entity.Category;
import br.edu.ifpb.dac.entity.Product;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.exception.*;
import br.edu.ifpb.dac.mapper.ProductMapper;
import br.edu.ifpb.dac.repository.CategoryRepository;
import br.edu.ifpb.dac.repository.ProductRepository;;
import br.edu.ifpb.dac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ProductDTO save(ProductDTO dto) {
        try {
            User user = getAuthenticatedUser();

            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

            Product product = mapper.toEntity(dto, user, category);
            Product saved = repository.save(product);
            return mapper.toDTO(saved);
        } catch (Exception e) {
            throw new ProductPersistenceException("Erro ao cadastrar o produto: " + e.getMessage());
        }
    }

    public List<ProductDTO> findAll() {
        User user = getAuthenticatedUser();
        return repository.findByUserId(user.getId()).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));
        return mapper.toDTO(product);
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product existing = repository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

            existing.setTitle(dto.getTitle());
            existing.setDescription(dto.getDescription());
            existing.setPrice(dto.getPrice());
            existing.setCategory(category);
            existing.setQuantity(dto.getQuantity());
            existing.setForExchange(dto.isForExchange());
            existing.setImageUrl(dto.getImageUrl());

            Product updated = repository.save(existing);
            return mapper.toDTO(updated);
        } catch (Exception e) {
            throw new ProductPersistenceException("Erro ao atualizar o produto: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

        if (product.getAdvertisements() != null && !product.getAdvertisements().isEmpty()) {
            throw new ProductHasAdvertisementException("Produto não pode ser deletado porque está vinculado a um anúncio.");
        }

        repository.delete(product);
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário autenticado não encontrado"));
    }
}