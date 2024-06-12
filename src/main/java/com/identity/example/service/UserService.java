package com.identity.example.service;

import com.identity.example.dto.ProjectRequestDTO;
import com.identity.example.dto.UserDetailDTO;
import com.identity.example.dto.UserRequestDTO;
import com.identity.example.dto.UserResponseDTO;
import java.util.List;
import java.util.UUID;

public interface UserService {

  UserResponseDTO createUser(UserRequestDTO userRequestDTO);

  List<UserResponseDTO> getAllUsers();

  UserResponseDTO getUser(UUID uuid);

  UserDetailDTO getUserDetail(UUID uuid);

  void deleteUser(UUID uuid);

  UserDetailDTO assignNewProject(UUID userUuid, ProjectRequestDTO projectRequestDTO);

  UserDetailDTO assignExistingProject(UUID userUuid, UUID projectUuid);

}
