package br.edu.ifpb.dac.config;

import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.enums.Role;
import br.edu.ifpb.dac.repository.UserRepository;
import br.edu.ifpb.dac.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin@ifpb.edu.br").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin@ifpb.edu.br");
                admin.setPassword("admin123");
                admin.setFullName("Administrador");
                admin.setEnrollmentNumber("0001");
                admin.setRoles(List.of(Role.ROLE_ADMIN));
                userService.registerAdm(admin);
                System.out.println("Usuário admin criado com sucesso!");
            }
        };
    }
}
