package br.edu.ifpb.dac.service;

import br.edu.ifpb.dac.entity.Category;
import br.edu.ifpb.dac.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        return repository.findAll();
    }
}