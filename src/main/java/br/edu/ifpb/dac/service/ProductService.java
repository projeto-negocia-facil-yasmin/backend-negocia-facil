package br.edu.ifpb.dac.service;

import br.edu.ifpb.dac.dto.ProductDTO;
import br.edu.ifpb.dac.entity.Product;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.exception.ProductDeleteException;
import br.edu.ifpb.dac.exception.ProductNotFoundException;
import br.edu.ifpb.dac.exception.ProductPersistenceException;
import br.edu.ifpb.dac.mapper.ProductMapper;
import br.edu.ifpb.dac.repository.ProductRepository;
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
    private final UserRepository userRepository;
    private final ProductMapper mapper;

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ProductNotFoundException("Usuário autenticado não encontrado"));
    }

    public ProductDTO save(ProductDTO dto) {
        try {
            User user = getAuthenticatedUser();
            Product product = mapper.toEntity(dto, user);
            Product saved = repository.save(product);
            return mapper.toDTO(saved);
        } catch (Exception e) {
            throw new ProductPersistenceException("Erro ao cadastrar o produto: " + e.getMessage());
        }
    }

    public List<ProductDTO> findAll() {
        if (isAdmin()) {
            return repository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            User user = getAuthenticatedUser();
            return repository.findAll().stream()
                    .filter(p -> p.getUser().getId().equals(user.getId()))
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public ProductDTO findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));
        User user = getAuthenticatedUser();
        if (!product.getUser().getId().equals(user.getId())) {
            throw new ProductNotFoundException("Produto não encontrado");
        }
        return mapper.toDTO(product);
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product existing = repository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

            User user = getAuthenticatedUser();

            if (!isAdmin() && !existing.getUser().getId().equals(user.getId())) {
                throw new ProductPersistenceException("Você não tem permissão para editar este produto");
            }

            existing.setTitle(dto.getTitle());
            existing.setDescription(dto.getDescription());
            existing.setPrice(dto.getPrice());
            existing.setCategory(dto.getCategory());
            existing.setQuantity(dto.getQuantity());
            existing.setForExchange(dto.isForExchange());

            Product updated = repository.save(existing);
            return mapper.toDTO(updated);
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ProductPersistenceException("Erro ao atualizar o produto: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));
        User user = getAuthenticatedUser();

        if (!isAdmin() && !product.getUser().getId().equals(user.getId())) {
            throw new ProductDeleteException("Você não tem permissão para deletar este produto");
        }

        try {
            repository.delete(product);
        } catch (Exception e) {
            throw new ProductDeleteException("Erro ao deletar o produto");
        }
    }

    private boolean isAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}