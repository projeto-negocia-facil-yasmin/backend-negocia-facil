package br.edu.ifpb.dac.service;

import br.edu.ifpb.dac.dto.RuleDTO;
import br.edu.ifpb.dac.entity.Rule;
import br.edu.ifpb.dac.mapper.RuleMapper;
import br.edu.ifpb.dac.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RuleService {

    private final RuleRepository repository;

    public RuleService(RuleRepository repository) {
        this.repository = repository;
    }

    public List<RuleDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(RuleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<RuleDTO> findById(Long id) {
        return repository.findById(id).map(RuleMapper::toDTO);
    }

    public RuleDTO save(RuleDTO dto) {
        Rule rule = RuleMapper.toEntity(dto);
        Rule saved = repository.save(rule);
        return RuleMapper.toDTO(saved);
    }

    public RuleDTO update(Long id, RuleDTO dto) {
        Rule rule = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rule not found"));

        RuleMapper.updateEntity(rule, dto);
        return RuleMapper.toDTO(repository.save(rule));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Rule not found");
        }
        repository.deleteById(id);
    }
}
