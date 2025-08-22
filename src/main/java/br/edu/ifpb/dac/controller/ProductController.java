package br.edu.ifpb.dac.controller;

import br.edu.ifpb.dac.dto.ProductDTO;
import br.edu.ifpb.dac.exception.ProductHasAdvertisementException;
import br.edu.ifpb.dac.exception.ProductNotFoundException;
import br.edu.ifpb.dac.exception.ProductPersistenceException;
import br.edu.ifpb.dac.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO dto) {
        ProductDTO savedProduct = service.save(dto);
        return ResponseEntity.status(201).body(savedProduct);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> listAll() {
        List<ProductDTO> products = service.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        try {
            ProductDTO product = service.findById(id);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        try {
            ProductDTO updated = service.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", e.getMessage()));
        } catch (ProductPersistenceException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", e.getMessage()));
        } catch (ProductHasAdvertisementException e) {
            return ResponseEntity.status(409)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}