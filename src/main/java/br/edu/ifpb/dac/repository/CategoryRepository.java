package br.edu.ifpb.dac.repository;

import br.edu.ifpb.dac.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}