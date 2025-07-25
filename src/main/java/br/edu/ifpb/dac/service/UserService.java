package br.edu.ifpb.dac.service;

import br.edu.ifpb.dac.dto.UpdateUserResponseDTO;
import br.edu.ifpb.dac.dto.UserDTO;
import br.edu.ifpb.dac.dto.UserResponseDTO;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.enums.Role;
import br.edu.ifpb.dac.exception.CannotDeleteAdminException;
import br.edu.ifpb.dac.exception.UserNotFoundException;
import br.edu.ifpb.dac.mapper.UserMapper;
import br.edu.ifpb.dac.repository.UserRepository;
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
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of(Role.ROLE_USER));
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
        user.setRoles(List.of(Role.ROLE_USER));
        userRepository.save(user);
        return UserMapper.toUserResponseDTO(user);
    }

    public void delete(Long id) {
        User user = findUserById(id);
        verifyIsNotAdmin(user);
        userRepository.delete(user);
    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toUserResponseDTO);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = findUserById(id);
        return UserMapper.toUserResponseDTO(user);
    }

    public UpdateUserResponseDTO updateUser(Long id, UserDTO userDTO) {
        User user = findUserById(id);
        verifyIsNotAdmin(user);
        updateUserFromDTO(user,userDTO);
        userRepository.save(user);
        return UserMapper.toUpdateUserResponseDTO(user);
    }

    private void updateUserFromDTO(User user, UserDTO userDTO) {
        user.setUsername(userDTO.username());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setFullName(userDTO.fullName());
        user.setEnrollmentNumber(userDTO.enrollmentNumber());
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("user with id " + id + " not found")
        );
    }

    private void verifyIsNotAdmin(User user) {
        if(user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new CannotDeleteAdminException("Cannot proceed action on admin user: " + user.getUsername());
        }
    }
}
