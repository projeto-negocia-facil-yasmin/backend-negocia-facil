package br.edu.ifpb.dac.controller;

import br.edu.ifpb.dac.dto.UserDTO;
import br.edu.ifpb.dac.mapper.UserMapper;
import br.edu.ifpb.dac.service.UserService;
import br.edu.ifpb.dac.dto.AuthRequest;
import br.edu.ifpb.dac.dto.AuthResponse;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.service.UserDetailsServiceImpl;
import br.edu.ifpb.dac.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO request) {
        try {
            User user = UserMapper.toEntity(request);
            userService.register(user);
            return ResponseEntity.status(201).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtUtil.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();

        return ResponseEntity.ok(new AuthResponse(token, roles));
    }
}