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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final UserRepository userRepository;
    private final ProductMapper mapper;

    public ProductDTO save(ProductDTO dto) {
        try {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ProductNotFoundException("Usuário não encontrado"));
            Product product = mapper.toEntity(dto, user);
            Product saved = repository.save(product);
            return mapper.toDTO(saved);
        } catch (Exception e) {
            throw new ProductPersistenceException("Erro ao cadastrar o produto: " + e.getMessage());
        }
    }

    public List<ProductDTO> findAll() {
        return repository.findAll().stream()
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

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ProductNotFoundException("Usuário não encontrado"));

            existing.setTitle(dto.getTitle());
            existing.setDescription(dto.getDescription());
            existing.setPrice(dto.getPrice());
            existing.setCategory(dto.getCategory());
            existing.setQuantity(dto.getQuantity());
            existing.setForExchange(dto.isForExchange());
            existing.setUser(user);

            Product updated = repository.save(existing);
            return mapper.toDTO(updated);
        } catch (Exception e) {
            throw new ProductPersistenceException("Erro ao atualizar o produto: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));
        try {
            repository.delete(product);
        } catch (Exception e) {
            throw new ProductDeleteException("Erro ao deletar o produto");
        }
    }
}