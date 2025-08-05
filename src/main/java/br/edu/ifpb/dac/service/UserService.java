package br.edu.ifpb.dac.service;

import br.edu.ifpb.dac.dto.UpdateUserResponseDTO;
import br.edu.ifpb.dac.dto.UserDTO;
import br.edu.ifpb.dac.dto.UserResponseDTO;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.enums.Role;
import br.edu.ifpb.dac.exception.CannotDeleteAdminException;
import br.edu.ifpb.dac.exception.LastAdminDeletionException;
import br.edu.ifpb.dac.exception.UnauthorizedUserEditException;
import br.edu.ifpb.dac.exception.UserNotFoundException;
import br.edu.ifpb.dac.mapper.UserMapper;
import br.edu.ifpb.dac.repository.UserRepository;
import br.edu.ifpb.dac.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String email = user.getUsername().toLowerCase();
        if (email.endsWith("@ifpb.edu.br")) {
            user.setRoles(List.of(Role.ROLE_ADMIN));
        } else if (email.endsWith("@academico.ifpb.edu.br")) {
            user.setRoles(List.of(Role.ROLE_USER));
        } else {
            throw new RuntimeException("Email inválido para cadastro.");
        }

        userRepository.save(user);
    }

    public void registerAdm(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public UserResponseDTO createUser(UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String email = user.getUsername().toLowerCase();
        if (email.endsWith("@ifpb.edu.br")) {
            user.setRoles(List.of(Role.ROLE_ADMIN));
        } else if (email.endsWith("@academico.ifpb.edu.br")) {
            user.setRoles(List.of(Role.ROLE_USER));
        } else {
            throw new RuntimeException("Email inválido para cadastro.");
        }

        userRepository.save(user);
        return UserMapper.toUserResponseDTO(user);
    }

    public void delete(Long id) {
        User targetUser = findUserById(id);
        User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);

        boolean isAdmin = authenticatedUser.getRoles().contains(Role.ROLE_ADMIN);
        if (!isAdmin) {
            throw new SecurityException("Você não tem permissão para deletar este usuário.");
        }

        if (targetUser.getRoles().contains(Role.ROLE_ADMIN)) {
            long adminCount = userRepository.countByRolesContaining(Role.ROLE_ADMIN);
            if (adminCount <= 1) {
                throw new LastAdminDeletionException("Não é possível deletar o último administrador.");
            }
        }

        userRepository.delete(targetUser);
    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toUserResponseDTO);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = findUserById(id);
        return UserMapper.toUserResponseDTO(user);
    }

    public UpdateUserResponseDTO updateUser(Long id, UserDTO userDTO) {
        User targetUser = findUserById(id);
        User authenticatedUser = SecurityUtils.getAuthenticatedUser(userRepository);

        boolean isSelf = authenticatedUser.getId().equals(targetUser.getId());

        if (!isSelf) {
            throw new UnauthorizedUserEditException("Você não pode editar outro usuário.");
        }

        targetUser.setUsername(userDTO.username());
        targetUser.setPassword(passwordEncoder.encode(userDTO.password()));
        targetUser.setFullName(userDTO.fullName());
        targetUser.setEnrollmentNumber(userDTO.enrollmentNumber());

        userRepository.save(targetUser);
        return UserMapper.toUpdateUserResponseDTO(targetUser);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("user with id " + id + " not found")
        );
    }
}