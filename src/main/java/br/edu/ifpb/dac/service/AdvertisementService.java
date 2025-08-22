package br.edu.ifpb.dac.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import br.edu.ifpb.dac.dto.UserResponseDTO;
import br.edu.ifpb.dac.util.SecurityUtils;
import org.springframework.stereotype.Service;
import br.edu.ifpb.dac.dto.AdvertisementRequestDTO;
import br.edu.ifpb.dac.dto.AdvertisementResponseDTO;
import br.edu.ifpb.dac.dto.ProductDTO;
import br.edu.ifpb.dac.entity.Advertisement;
import br.edu.ifpb.dac.entity.Product;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.exception.AdvertisementNotFoundException;
import br.edu.ifpb.dac.exception.AdvertisementPersistenceException;
import br.edu.ifpb.dac.exception.ProductNotFoundException;
import br.edu.ifpb.dac.exception.UnauthorizedAdvertisementEditException;
import br.edu.ifpb.dac.exception.UserNotFoundException;
import br.edu.ifpb.dac.mapper.AdvertisementMapper;
import br.edu.ifpb.dac.repository.AdvertisementRepository;
import br.edu.ifpb.dac.repository.ProductRepository;
import br.edu.ifpb.dac.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final ProductRepository productRepository;
    private final AdvertisementMapper advertisementMapper;
    private final UserService userService;

    public void saveAdvertisement(AdvertisementRequestDTO advertisementDTO) {
        try {
            Advertisement advertisement = advertisementMapper.toEntity(advertisementDTO);
            User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);
            advertisement.setAdvertiser(authenticatedUser);
            advertisementRepository.save(advertisement);
        } catch (Exception e) {
            throw new AdvertisementPersistenceException("Erro ao salvar anúncio: " + e.getMessage());
        }
    }

    public List<AdvertisementResponseDTO> getAllAdvertisements() {
        List<Advertisement> ads = advertisementRepository.findAll();
        return ads.stream()
                .map(advertisementMapper::toResponseDTO)
                .toList();
    }

    public AdvertisementResponseDTO getAdvertisementById(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new AdvertisementNotFoundException("Anúncio com id " + id + " não encontrado"));
        return advertisementMapper.toResponseDTO(advertisement);
    }

    public void deleteAdvertisement(Long id) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new AdvertisementNotFoundException("Anúncio com id " + id + " não encontrado"));

        User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);
        if (!advertisement.getAdvertiser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAdvertisementEditException("Você não tem permissão para excluir este anúncio");
        }

        advertisementRepository.delete(advertisement);
    }

    public void updateAdvertisement(Long id, AdvertisementResponseDTO advertisementDTO) {
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new AdvertisementNotFoundException("Anúncio com id " + id + " não encontrado"));

        User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);
        if (!advertisement.getAdvertiser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAdvertisementEditException("Você não tem permissão para editar este anúncio");
        }

        User advertiser = userRepository.findById(advertisementDTO.advertiser().id())
                .orElseThrow(() -> new UserNotFoundException("Usuário com id " + advertisementDTO.advertiser().id() + " não encontrado"));

        List<Product> products = new ArrayList<>();
        for (ProductDTO productDTO : advertisementDTO.products()) {
            Product product = productRepository.findById(productDTO.getId())
                    .orElseThrow(() -> new ProductNotFoundException("Produto com id " + productDTO.getId() + " não encontrado"));
            products.add(product);
        }

        advertisement.setAdvertiser(advertiser);
        advertisement.setCreatedAt(LocalDateTime.now());
        advertisement.setProducts(products);

        try {
            advertisementRepository.save(advertisement);
        } catch (Exception e) {
            throw new AdvertisementPersistenceException("Erro ao atualizar anúncio: " + e.getMessage());
        }
    }

    public UserResponseDTO getAdvertiserByAdvertisementId(Long advertisementId) {
        Advertisement ad = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new AdvertisementNotFoundException("Anúncio com id " + advertisementId + " não encontrado"));
        Long advertiserId = ad.getAdvertiser().getId();
        return userService.getUserById(advertiserId);
    }
}