package br.edu.ifpb.dac.mapper;

import br.edu.ifpb.dac.dto.UpdateUserResponseDTO;
import br.edu.ifpb.dac.dto.UserDTO;
import br.edu.ifpb.dac.dto.AdvertisementOwnerDTO;
import br.edu.ifpb.dac.dto.UserResponseDTO;
import br.edu.ifpb.dac.entity.User;

public final class UserMapper {

    public static UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getImgUrl(),
                user.getFullName(),
                user.getEnrollmentNumber());
    }

    public static User toEntity(UserDTO dto) {
        return new User(
                dto.username(),
                dto.password(),
                dto.fullName(),
                dto.enrollmentNumber());
    }

    public static User toEntity(AdvertisementOwnerDTO advertisementOwnerDTO) {
        return new User(
                advertisementOwnerDTO.id(),
                advertisementOwnerDTO.username(),
                advertisementOwnerDTO.imgUrl(),
                advertisementOwnerDTO.fullName(),
                advertisementOwnerDTO.enrollmentNumber());
    }

    public static AdvertisementOwnerDTO toAdvertisementOwnerDTO(User user) {
        return new AdvertisementOwnerDTO(
                user.getId(),
                user.getUsername(),
                user.getImgUrl(),
                user.getFullName(),
                user.getEnrollmentNumber());
    }

    public static UpdateUserResponseDTO toUpdateUserResponseDTO(User user) {
        return new UpdateUserResponseDTO(
              user.getId(),
              user.getUsername(),
              user.getFullName(),
              user.getEnrollmentNumber()
        );
    }
}