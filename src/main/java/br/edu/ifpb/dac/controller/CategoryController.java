package br.edu.ifpb.dac.controller;

import br.edu.ifpb.dac.entity.Category;
import br.edu.ifpb.dac.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<Category> getAllCategories() {
        return service.findAll();
    }
}