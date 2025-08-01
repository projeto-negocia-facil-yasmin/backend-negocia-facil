package br.edu.ifpb.dac.repository;

import br.edu.ifpb.dac.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}