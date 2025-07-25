package br.edu.ifpb.dac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.dac.entity.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>{

}
