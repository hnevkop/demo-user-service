package com.identity.example.service;

import com.identity.example.dto.UserDetailDTO;
import com.identity.example.dto.UserRequestDTO;
import com.identity.example.dto.UserResponseDTO;
import com.identity.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    uses = {com.identity.example.service.ProjectMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  UserResponseDTO userToUserResponseDto(User user);

  @Mapping(source = "projects", target = "projects")
  UserDetailDTO userToUserDetailDto(User user);

  User userRequestDtoToUser(UserRequestDTO userRequestDto);


}
