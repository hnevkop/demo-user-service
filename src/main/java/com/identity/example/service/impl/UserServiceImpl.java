package com.identity.example.service.impl;

import com.identity.example.dto.ProjectRequestDTO;
import com.identity.example.dto.UserDetailDTO;
import com.identity.example.dto.UserRequestDTO;
import com.identity.example.dto.UserResponseDTO;
import com.identity.example.exception.ProjectNotFoundException;
import com.identity.example.exception.UserNotFoundException;
import com.identity.example.model.User;
import com.identity.example.persistence.ProjectRepository;
import com.identity.example.persistence.UserRepository;
import com.identity.example.service.ProjectMapper;
import com.identity.example.service.UserMapper;
import com.identity.example.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final UserMapper userMapper;
  private final ProjectMapper projectMapper;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(
      UserRepository userRepository,
      ProjectRepository projectRepository,
      UserMapper userMapper,
      ProjectMapper projectMapper) {
    this.userRepository = userRepository;
    this.projectRepository = projectRepository;
    this.userMapper = userMapper;
    this.projectMapper = projectMapper;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
    var user = createUserFromDto(userRequestDTO);
    var updated = userRepository.save(user);
    return userMapper.userToUserResponseDto(updated);
  }

  public List<UserResponseDTO> getAllUsers() {
    return userRepository.findAll().stream().map(userMapper::userToUserResponseDto).toList();
  }

  public UserResponseDTO getUser(UUID uuid) {
    Optional<User> userOpt = userRepository.findByUuid(uuid);
    return userOpt.map(userMapper::userToUserResponseDto).orElse(null);
  }

  @Override
  public UserDetailDTO getUserDetail(UUID uuid) {
    Optional<User> userOpt = userRepository.findByUuidWithProjects(uuid);
    return userOpt.map(userMapper::userToUserDetailDto).orElse(null);
  }

  @Transactional
  public void deleteUser(UUID uuid) {
    userRepository
        .findByUuid(uuid)
        .ifPresent(
            user -> {
              user.getProjects().clear();
              userRepository.delete(user);
            });
    }

  @Override
  @Transactional
  public UserDetailDTO assignNewProject(UUID userUuid, ProjectRequestDTO projectRequestDTO) {
    var user = userRepository.findByUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));
    var project = projectMapper.projectDtoToProject(projectRequestDTO);
    user.getProjects().add(project);
    userRepository.save(user);
    log.info("User got a new project {}", project);
    return userMapper.userToUserDetailDto(user);
  }

  @Override
  @Transactional
  public UserDetailDTO assignExistingProject(UUID userUuid, UUID projectUuid) {
    var user = userRepository.findByUuid(userUuid).orElseThrow(() -> new UserNotFoundException(userUuid));
    var project =
        projectRepository
            .findByUuid(projectUuid)
            .orElseThrow(() -> new ProjectNotFoundException(projectUuid));

    user.getProjects().add(project);
    projectRepository.save(project);
    userRepository.save(user);
    log.info("User is now assigned a project {}", projectUuid);
    return userMapper.userToUserDetailDto(user);
  }

  private User createUserFromDto(UserRequestDTO userRequestDTO) {
    var encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
    userRequestDTO.setPassword(encodedPassword);
    return userMapper.userRequestDtoToUser(userRequestDTO);
  }
}
