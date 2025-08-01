package br.edu.ifpb.dac.mapper;

import br.edu.ifpb.dac.dto.RuleDTO;
import br.edu.ifpb.dac.entity.Rule;

public class RuleMapper {

    public static RuleDTO toDTO(Rule rule) {
        return new RuleDTO(
            rule.getId(),
            rule.getTitle(),
            rule.getDescription(),
            rule.isActive()
        );
    }

    public static Rule toEntity(RuleDTO dto) {
        Rule rule = new Rule();
        rule.setId(dto.getId());
        rule.setTitle(dto.getTitle());
        rule.setDescription(dto.getDescription());
        rule.setActive(dto.isActive());
        return rule;
    }

    public static void updateEntity(Rule rule, RuleDTO dto) {
        rule.setTitle(dto.getTitle());
        rule.setDescription(dto.getDescription());
        rule.setActive(dto.isActive());
    }
}