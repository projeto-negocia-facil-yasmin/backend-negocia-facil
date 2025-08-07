package br.edu.ifpb.dac.controller;

import java.util.List;
import br.edu.ifpb.dac.dto.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.ifpb.dac.dto.AdvertisementRequestDTO;
import br.edu.ifpb.dac.dto.AdvertisementResponseDTO;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.service.AdvertisementService;
import br.edu.ifpb.dac.util.SecurityUtils;
import br.edu.ifpb.dac.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<AdvertisementRequestDTO> createAdvertisement(@RequestBody AdvertisementRequestDTO advertisementDTO) {
        advertisementService.saveAdvertisement(advertisementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(advertisementDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdvertisementResponseDTO>> getAllAdvertisements() {
        User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);
        List<AdvertisementResponseDTO> advertisements = advertisementService.getAllAdvertisements();

        if (advertisements.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(advertisements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponseDTO> getAdvertisementById(@PathVariable Long id) {
        try {
            AdvertisementResponseDTO advertisement = advertisementService.getAdvertisementById(id);
            return ResponseEntity.ok(advertisement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdvertisement(@PathVariable Long id) {
        try {
            advertisementService.deleteAdvertisement(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdvertisement(@PathVariable Long id, @RequestBody AdvertisementResponseDTO advertisementDTO) {
        try {
            advertisementService.updateAdvertisement(id, advertisementDTO);
            return ResponseEntity.ok(advertisementDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/advertiser")
    public ResponseEntity<UserResponseDTO> getAdvertiserByAdvertisementId(@PathVariable Long id) {
        UserResponseDTO advertiser = advertisementService.getAdvertiserByAdvertisementId(id);
        return ResponseEntity.ok(advertiser);
    }
}