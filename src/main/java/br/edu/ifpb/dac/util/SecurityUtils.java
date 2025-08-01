package br.edu.ifpb.dac.util;

import org.springframework.security.core.context.SecurityContextHolder;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.repository.UserRepository;

public class SecurityUtils {

    public static boolean isAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public static User getAuthenticatedUser(UserRepository userRepository) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String username = auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
    }
}