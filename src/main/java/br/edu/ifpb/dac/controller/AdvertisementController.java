package br.edu.ifpb.dac.controller;

import java.util.List;
import java.util.Map;

import br.edu.ifpb.dac.dto.UserResponseDTO;
import br.edu.ifpb.dac.dto.AdvertisementRequestDTO;
import br.edu.ifpb.dac.dto.AdvertisementResponseDTO;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.exception.AdvertisementNotFoundException;
import br.edu.ifpb.dac.exception.AdvertisementPersistenceException;
import br.edu.ifpb.dac.exception.UnauthorizedAdvertisementEditException;
import br.edu.ifpb.dac.repository.UserRepository;
import br.edu.ifpb.dac.service.AdvertisementService;
import br.edu.ifpb.dac.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createAdvertisement(@RequestBody AdvertisementRequestDTO advertisementDTO) {
        try {
            advertisementService.saveAdvertisement(advertisementDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(advertisementDTO);
        } catch (AdvertisementPersistenceException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdvertisementResponseDTO>> getAllAdvertisements() {
        List<AdvertisementResponseDTO> advertisements = advertisementService.getAllAdvertisements();
        if (advertisements.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(advertisements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdvertisementById(@PathVariable Long id) {
        try {
            AdvertisementResponseDTO advertisement = advertisementService.getAdvertisementById(id);
            return ResponseEntity.ok(advertisement);
        } catch (AdvertisementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdvertisement(@PathVariable Long id) {
        try {
            advertisementService.deleteAdvertisement(id);
            return ResponseEntity.noContent().build();
        } catch (AdvertisementNotFoundException | UnauthorizedAdvertisementEditException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdvertisement(@PathVariable Long id, @RequestBody AdvertisementResponseDTO advertisementDTO) {
        try {
            advertisementService.updateAdvertisement(id, advertisementDTO);
            return ResponseEntity.ok(advertisementDTO);
        } catch (AdvertisementNotFoundException | AdvertisementPersistenceException | UnauthorizedAdvertisementEditException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}/advertiser")
    public ResponseEntity<?> getAdvertiserByAdvertisementId(@PathVariable Long id) {
        try {
            UserResponseDTO advertiser = advertisementService.getAdvertiserByAdvertisementId(id);
            return ResponseEntity.ok(advertiser);
        } catch (AdvertisementNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
}