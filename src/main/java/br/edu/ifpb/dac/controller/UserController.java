package br.edu.ifpb.dac.controller;

import br.edu.ifpb.dac.dto.UserDTO;
import br.edu.ifpb.dac.dto.UserResponseDTO;
import br.edu.ifpb.dac.dto.UpdateUserResponseDTO;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.mapper.UserMapper;
import br.edu.ifpb.dac.repository.UserRepository;
import br.edu.ifpb.dac.service.UserService;
import br.edu.ifpb.dac.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        User createdUser = userService.getOrCreateUserFromSuap(
                userDTO.username(),
                userDTO.fullName(),
                userDTO.enrollmentNumber(),
                userDTO.phone(),
                userDTO.imgUrl()
        );

        UserResponseDTO createdUserDTO = UserMapper.toUserResponseDTO(createdUser);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUserDTO.id())
                .toUri();

        return ResponseEntity.created(uri).body(createdUserDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponseDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getLoggedUser() {
        User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);
        UserResponseDTO userDTO = UserMapper.toUserResponseDTO(authenticatedUser);
        return ResponseEntity.ok(userDTO);
    }
}