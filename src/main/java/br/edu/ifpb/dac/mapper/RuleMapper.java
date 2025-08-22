package br.edu.ifpb.dac.mapper;

import br.edu.ifpb.dac.dto.RuleDTO;
import br.edu.ifpb.dac.entity.Rule;
import br.edu.ifpb.dac.entity.User;

public class RuleMapper {

    public static RuleDTO toDTO(Rule rule) {
        return new RuleDTO(
                rule.getId(),
                rule.getTitle(),
                rule.getDescription(),
                rule.isActive(),
                rule.getUser() != null ? rule.getUser().getId() : null
        );
    }

    public static Rule toEntity(RuleDTO dto, User user) {
        Rule rule = new Rule();
        rule.setTitle(dto.getTitle());
        rule.setDescription(dto.getDescription());
        rule.setActive(dto.isActive());
        rule.setUser(user);
        return rule;
    }

    public static void updateEntity(Rule rule, RuleDTO dto, User user) {
        rule.setTitle(dto.getTitle());
        rule.setDescription(dto.getDescription());
        rule.setActive(dto.isActive());
        rule.setUser(user);
    }
}