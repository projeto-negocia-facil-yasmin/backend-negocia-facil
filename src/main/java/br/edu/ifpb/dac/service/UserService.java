package br.edu.ifpb.dac.service;

import br.edu.ifpb.dac.dto.UpdateUserResponseDTO;
import br.edu.ifpb.dac.dto.UserDTO;
import br.edu.ifpb.dac.dto.UserResponseDTO;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.enums.Role;
import br.edu.ifpb.dac.exception.LastAdminDeletionException;
import br.edu.ifpb.dac.exception.UnauthorizedUserEditException;
import br.edu.ifpb.dac.exception.UserNotFoundException;
import br.edu.ifpb.dac.mapper.UserMapper;
import br.edu.ifpb.dac.repository.UserRepository;
import br.edu.ifpb.dac.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getOrCreateUserFromSuap(String username,
                                        String fullName,
                                        String enrollmentNumber,
                                        String phone,
                                        String imgUrl) {

        return userRepository.findByEnrollmentNumber(enrollmentNumber)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setFullName(fullName);
                    newUser.setEnrollmentNumber(enrollmentNumber);
                    newUser.setPhone(phone);
                    newUser.setImgUrl(imgUrl);

                    if (username.toLowerCase().endsWith("@ifpb.edu.br")) {
                        newUser.setRoles(List.of(Role.ROLE_ADMIN));
                    } else {
                        newUser.setRoles(List.of(Role.ROLE_USER));
                    }

                    return userRepository.save(newUser);
                });
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
        targetUser.setFullName(userDTO.fullName());
        targetUser.setEnrollmentNumber(userDTO.enrollmentNumber());
        targetUser.setPhone(userDTO.phone());
        targetUser.setImgUrl(userDTO.imgUrl());

        userRepository.save(targetUser);
        return UserMapper.toUpdateUserResponseDTO(targetUser);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("user with id " + id + " not found")
        );
    }

    public UserDetails toUserDetails(br.edu.ifpb.dac.entity.User appUser) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(appUser.getUsername())
                .password("") // senha não é necessária, SUAP já validou
                .authorities(appUser.getRoles().stream()
                        .map(Enum::name)
                        .toArray(String[]::new))
                .build();
    }
}