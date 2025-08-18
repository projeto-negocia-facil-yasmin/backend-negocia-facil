package br.edu.ifpb.dac.controller;

import br.edu.ifpb.dac.entity.Category;
import br.edu.ifpb.dac.service.CategoryService;
import br.edu.ifpb.dac.exception.CategoryPersistenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> categories = service.findAll();
            if (categories.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            throw new CategoryPersistenceException("Erro ao listar categorias: " + e.getMessage());
        }
    }
}