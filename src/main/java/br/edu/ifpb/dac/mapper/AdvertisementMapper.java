package br.edu.ifpb.dac.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import br.edu.ifpb.dac.dto.AdvertisementRequestDTO;
import br.edu.ifpb.dac.dto.AdvertisementResponseDTO;
import br.edu.ifpb.dac.dto.ProductDTO;
import br.edu.ifpb.dac.entity.Advertisement;
import br.edu.ifpb.dac.entity.Category;
import br.edu.ifpb.dac.entity.Product;
import br.edu.ifpb.dac.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdvertisementMapper {

    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public AdvertisementResponseDTO toResponseDTO(Advertisement advertisement) {
        return new AdvertisementResponseDTO(
                advertisement.getId(),
                UserMapper.toAdvertisementOwnerDTO(advertisement.getAdvertiser()),

                advertisement.getProducts().stream()
                        .map(productMapper::toDTO)
                        .collect(Collectors.toList()),

                advertisement.getTotalPrice(),
                advertisement.getCreatedAt()
        );
    }

    public Advertisement toEntity(AdvertisementRequestDTO advertisementDTO) {
        Advertisement advertisement = new Advertisement();
        advertisement.setAdvertiser(UserMapper.toEntity(advertisementDTO.advertiser()));

        List<Product> products = new ArrayList<>();
        for (ProductDTO productDTO : advertisementDTO.products()) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada para o produto: " + productDTO.getTitle()));

            products.add(productMapper.toEntity(productDTO, advertisement.getAdvertiser(), category));
        }

        advertisement.setProducts(products);
        advertisement.setCreatedAt(advertisementDTO.createdAt());
        return advertisement;
    }
}