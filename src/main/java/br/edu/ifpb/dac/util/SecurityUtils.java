package br.edu.ifpb.dac.util;

import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.exception.UserNotFoundException;
import br.edu.ifpb.dac.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private static Authentication getAuthenticationOrFail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new SecurityException("Usuário não autenticado");
        }
        return auth;
    }

    public static String getAuthenticatedUsername() {
        Authentication auth = getAuthenticationOrFail();
        return auth.getName();
    }

    public static boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public static User getAuthenticatedUser(UserRepository userRepository) {
        String username = getAuthenticatedUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário autenticado não encontrado"));
    }
}