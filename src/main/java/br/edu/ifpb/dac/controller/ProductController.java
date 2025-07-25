package br.edu.ifpb.dac.controller;

import br.edu.ifpb.dac.dto.ProductDTO;
import br.edu.ifpb.dac.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/create")
    public ProductDTO create(@RequestBody ProductDTO dto) {
        return service.save(dto);
    }

    @GetMapping("/list")
    public List<ProductDTO> listAll() {
        return service.findAll();
    }

    @GetMapping("/get/{id}")
    public ProductDTO getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/update/{id}")
    public ProductDTO update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}