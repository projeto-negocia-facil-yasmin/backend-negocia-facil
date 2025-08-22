package br.edu.ifpb.dac.mapper;

import br.edu.ifpb.dac.dto.UpdateUserResponseDTO;
import br.edu.ifpb.dac.dto.UserDTO;
import br.edu.ifpb.dac.dto.AdvertisementOwnerDTO;
import br.edu.ifpb.dac.dto.UserResponseDTO;
import br.edu.ifpb.dac.entity.User;
import br.edu.ifpb.dac.enums.Role;

public final class UserMapper {

    public static UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getImgUrl(),
                user.getFullName(),
                user.getEnrollmentNumber(),
                user.getPhone(),
                user.getRoles().stream().map(Role::name).toList()
        );
    }

    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setFullName(dto.fullName());
        user.setEnrollmentNumber(dto.enrollmentNumber());
        user.setPhone(dto.phone());
        user.setImgUrl(dto.imgUrl());
        return user;
    }

    public static User toEntity(AdvertisementOwnerDTO advertisementOwnerDTO) {
        User user = new User();
        user.setId(advertisementOwnerDTO.id());
        user.setUsername(advertisementOwnerDTO.username());
        user.setImgUrl(advertisementOwnerDTO.imgUrl());
        user.setFullName(advertisementOwnerDTO.fullName());
        user.setEnrollmentNumber(advertisementOwnerDTO.enrollmentNumber());
        return user;
    }

    public static AdvertisementOwnerDTO toAdvertisementOwnerDTO(User user) {
        return new AdvertisementOwnerDTO(
                user.getId(),
                user.getUsername(),
                user.getImgUrl(),
                user.getFullName(),
                user.getEnrollmentNumber()
        );
    }

    public static UpdateUserResponseDTO toUpdateUserResponseDTO(User user) {
        return new UpdateUserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEnrollmentNumber(),
                user.getPhone(),
                user.getImgUrl()
        );
    }
}