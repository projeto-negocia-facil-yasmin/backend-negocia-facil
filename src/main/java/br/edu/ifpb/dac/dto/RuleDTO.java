package br.edu.ifpb.dac.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleDTO {
    private Long id;
    private String title;
    private String description;
    private boolean active;
}
