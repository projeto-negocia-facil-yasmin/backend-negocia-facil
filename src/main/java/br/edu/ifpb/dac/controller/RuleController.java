package br.edu.ifpb.dac.controller;

import br.edu.ifpb.dac.dto.RuleDTO;
import br.edu.ifpb.dac.exception.RuleNotFoundException;
import br.edu.ifpb.dac.exception.RulePersistenceException;
import br.edu.ifpb.dac.service.RuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rules")
@CrossOrigin(origins = "*")
public class RuleController {

    private final RuleService service;

    public RuleController(RuleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RuleDTO>> getAll() {
        List<RuleDTO> rules = service.findAll();
        if (rules.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rules);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RuleDTO dto) {
        try {
            RuleDTO saved = service.save(dto);
            return ResponseEntity.status(201).body(saved);
        } catch (RulePersistenceException e) {
            return ResponseEntity.status(400)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RuleDTO dto) {
        try {
            RuleDTO updated = service.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuleNotFoundException | RulePersistenceException e) {
            return ResponseEntity.status(400)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuleNotFoundException | RulePersistenceException e) {
            return ResponseEntity.status(400)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}