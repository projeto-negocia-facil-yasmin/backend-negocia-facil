package br.edu.ifpb.dac.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.edu.ifpb.dac.dto.AdvertisementRequestDTO;
import br.edu.ifpb.dac.dto.AdvertisementResponseDTO;
import br.edu.ifpb.dac.dto.ProductDTO;
import br.edu.ifpb.dac.entity.Advertisement;
import br.edu.ifpb.dac.entity.Product;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdvertisementMapper {
    
    private static final ProductMapper productMapper = new ProductMapper();

    public static AdvertisementRequestDTO toRequestDTO(Advertisement advertisement) {
        return new AdvertisementRequestDTO(
            UserMapper.toAdvertisementOwnerDTO(advertisement.getAdvertiser()),

            advertisement.getProducts().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList()),

            advertisement.getDescription(),
            advertisement.getTotalPrice(),
            advertisement.getCreatedAt()
        );
    }

    public static AdvertisementResponseDTO toResponseDTO(Advertisement advertisement) {
        return new AdvertisementResponseDTO(
            advertisement.getId(),
            UserMapper.toAdvertisementOwnerDTO(advertisement.getAdvertiser()),

            advertisement.getProducts().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList()),

            advertisement.getDescription(),
            advertisement.getTotalPrice(),
            advertisement.getCreatedAt()
        );
    }

    //Cria uma entidade a partir de um DTO de requisição que não possui ID
    //pois ainda será criada no banco de dados
    public static Advertisement toEntity(AdvertisementRequestDTO advertisementDTO) {
        Advertisement advertisement = new Advertisement();
        advertisement.setAdvertiser(UserMapper.toEntity(advertisementDTO.advertiser()));

        List<Product> products = new ArrayList<>();
        for (ProductDTO productDTO : advertisementDTO.products()) {
            products.add(productMapper.toEntity(productDTO, advertisement.getAdvertiser()));
        }
        
        advertisement.setProducts(products);
        advertisement.setDescription(advertisementDTO.description());
        advertisement.setCreatedAt(advertisementDTO.createdAt());
        return advertisement;
    }

}
