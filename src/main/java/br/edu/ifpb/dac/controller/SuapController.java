package br.edu.ifpb.dac.controller;

import br.edu.ifpb.dac.dto.AuthRequest;
import br.edu.ifpb.dac.dto.UserDTO;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.service.UserService;
import br.edu.ifpb.dac.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/suap")
@RequiredArgsConstructor
public class SuapController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth")
    public ResponseEntity<?> authenticateSuap(@RequestBody AuthRequest request) {
        String matricula = request.enrollmentNumber();
        String password = request.password();

        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> suapLoginRequest = Map.of(
                    "username", matricula,
                    "password", password
            );
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> loginRequest = new HttpEntity<>(suapLoginRequest, headers);

            ResponseEntity<Map> loginResponse = restTemplate.postForEntity(
                    "https://suap.ifpb.edu.br/api/jwt/obtain_token/", loginRequest, Map.class
            );

            if (!loginResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(403).body("SUAP authentication failed: Invalid user or password");
            }

            String suapToken = (String) loginResponse.getBody().get("access");

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(403).body("SUAP authentication failed: Invalid user or password");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao conectar com o SUAP");
        }

        User user = userService.getOrCreateUserFromSuap(
                request.username(),
                request.fullName(),
                matricula,
                request.phone(),
                request.imgUrl()
        );

        String token = jwtUtil.generateToken(userService.toUserDetails(user));

        Map<String, Object> response = new HashMap<>();
        response.put("user", new UserDTO(
                user.getUsername(),
                user.getFullName(),
                user.getEnrollmentNumber(),
                user.getPhone(),
                user.getImgUrl()
        ));
        response.put("token", token);
        response.put("roles", user.getRoles());

        return ResponseEntity.ok(response);
    }
}