package br.edu.ifpb.dac.service;

import br.edu.ifpb.dac.dto.RuleDTO;
import br.edu.ifpb.dac.entity.Rule;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.exception.RuleNotFoundException;
import br.edu.ifpb.dac.exception.RulePersistenceException;
import br.edu.ifpb.dac.mapper.RuleMapper;
import br.edu.ifpb.dac.repository.RuleRepository;
import br.edu.ifpb.dac.repository.UserRepository;
import br.edu.ifpb.dac.util.SecurityUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleService {

    private final RuleRepository repository;
    private final UserRepository userRepository;

    public RuleService(RuleRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<RuleDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(RuleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RuleDTO save(RuleDTO dto) {
        try {
            User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);

            if (repository.existsByTitle(dto.getTitle())) {
                throw new RulePersistenceException("Uma regra com este título já existe");
            }

            Rule rule = RuleMapper.toEntity(dto, authenticatedUser);
            Rule saved = repository.save(rule);
            return RuleMapper.toDTO(saved);
        } catch (Exception e) {
            throw new RulePersistenceException("Erro ao criar regra: " + e.getMessage());
        }
    }

    public RuleDTO update(Long id, RuleDTO dto) {
        try {
            User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);

            Rule rule = repository.findById(id)
                    .orElseThrow(() -> new RuleNotFoundException("Regra com id " + id + " não encontrada"));

            RuleMapper.updateEntity(rule, dto, authenticatedUser);
            Rule updated = repository.save(rule);
            return RuleMapper.toDTO(updated);
        } catch (RuleNotFoundException | RulePersistenceException e) {
            throw e;
        } catch (Exception e) {
            throw new RulePersistenceException("Erro ao atualizar regra: " + e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);

            Rule rule = repository.findById(id)
                    .orElseThrow(() -> new RuleNotFoundException("Regra com id " + id + " não encontrada"));

            repository.delete(rule);
        } catch (RuleNotFoundException | RulePersistenceException e) {
            throw e;
        } catch (Exception e) {
            throw new RulePersistenceException("Erro ao deletar regra: " + e.getMessage());
        }
    }
}