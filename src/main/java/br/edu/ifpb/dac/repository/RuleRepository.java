package br.edu.ifpb.dac.repository;
import br.edu.ifpb.dac.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    boolean existsByTitle(String title);
}