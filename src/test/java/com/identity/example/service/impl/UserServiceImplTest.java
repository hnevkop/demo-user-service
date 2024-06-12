package com.identity.example.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.identity.example.dto.ProjectDTO;
import com.identity.example.dto.ProjectRequestDTO;
import com.identity.example.dto.UserDetailDTO;
import com.identity.example.dto.UserRequestDTO;
import com.identity.example.dto.UserResponseDTO;
import com.identity.example.exception.ProjectNotFoundException;
import com.identity.example.exception.UserNotFoundException;
import com.identity.example.model.Project;
import com.identity.example.model.User;
import com.identity.example.persistence.ProjectRepository;
import com.identity.example.persistence.UserRepository;
import com.identity.example.service.ProjectMapper;
import com.identity.example.service.UserMapper;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
  @Mock private UserRepository userRepository;

  @Mock private ProjectRepository projectRepository;

  @Mock private UserMapper userMapper;

  @Mock private ProjectMapper projectMapper;

  @InjectMocks private UserServiceImpl userService;

  private User user;
  private Project project;
  private UserRequestDTO userRequestDTO;
  private UserResponseDTO userResponseDTO;
  private UserDetailDTO userDetailDTO;
  private ProjectRequestDTO projectRequestDTO;

  @BeforeEach
  void setUp() {
    var userUuid = UUID.randomUUID();
    var projectUuid = UUID.randomUUID();

    user = new User();
    user.setName("username");
    user.setPassword("password");
    user.setProjects(new HashSet<>());
    user.setUuid(userUuid);

    project = new Project();
    project.setName("project");
    project.setUuid(projectUuid);

    userRequestDTO = new UserRequestDTO();
    userRequestDTO.setName("username");
    userRequestDTO.setPassword("password");
    userRequestDTO.setEmail("email@test.com");

    userResponseDTO = new UserResponseDTO();
    userResponseDTO.setUuid(userUuid);
    userResponseDTO.setName(user.getName());

    userDetailDTO = new UserDetailDTO();
    userDetailDTO.setUuid(userUuid);
    userDetailDTO.setName(user.getName());
    userDetailDTO.setProjects(Set.of(new ProjectDTO()));

    projectRequestDTO = new ProjectRequestDTO();
    projectRequestDTO.setName("project");
  }

  @Test
  void shouldCreateUser() {
    when(userMapper.userRequestDtoToUser(any((UserRequestDTO.class)))).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDTO);

    var createdUser = userService.createUser(userRequestDTO);

    assertNotNull(createdUser);
    assertEquals(user.getUuid(), createdUser.getUuid());
    assertEquals(user.getName(), createdUser.getName());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void shouldAssignNewProject() {
    when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
    when(projectMapper.projectDtoToProject(any(ProjectRequestDTO.class))).thenReturn(project);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.userToUserDetailDto(user)).thenReturn(userDetailDTO);

    var returnedUserDetailDto = userService.assignNewProject(user.getUuid(), projectRequestDTO);

    verify(userRepository, times(1)).findByUuid(user.getUuid());
    verify(userRepository, times(1)).save(user);
    assertEquals(userDetailDTO, returnedUserDetailDto);
  }

  @Test
  void shouldAssignExistingProject() {
    when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
    when(projectRepository.findByUuid(project.getUuid())).thenReturn(Optional.of(project));
    when(userMapper.userToUserDetailDto(user)).thenReturn(userDetailDTO);
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(projectRepository.save(any(Project.class))).thenReturn(project);

    var returnedUserDetailDto =
        userService.assignExistingProject(user.getUuid(), project.getUuid());

    verify(userRepository, times(1)).findByUuid(user.getUuid());
    verify(projectRepository, times(1)).findByUuid(project.getUuid());
    verify(userRepository, times(1)).save(user);
    verify(projectRepository, times(1)).save(project);
    assertEquals(userDetailDTO, returnedUserDetailDto);
  }

  @Test
  void shouldNotAssignNewProjectWhenUserNotFound() {
    when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> userService.assignNewProject(user.getUuid(), projectRequestDTO));

    verify(userRepository, times(1)).findByUuid(user.getUuid());
  }

  @Test
  void shouldNotAssignExistingProjectWhenUserNotFound() {
    when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.empty());

    assertThrows(
        UserNotFoundException.class,
        () -> userService.assignExistingProject(user.getUuid(), project.getUuid()));

    verify(userRepository, times(1)).findByUuid(user.getUuid());
  }

  @Test
  void shouldNotAssignExistingProjectWhenProjectNotFound() {
    when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
    when(projectRepository.findByUuid(project.getUuid())).thenReturn(Optional.empty());

    assertThrows(
        ProjectNotFoundException.class,
        () -> userService.assignExistingProject(user.getUuid(), project.getUuid()));

    verify(userRepository, times(1)).findByUuid(user.getUuid());
    verify(projectRepository, times(1)).findByUuid(project.getUuid());
  }

    @Test
    void shouldDeleteUser() {
        when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getUuid());

        verify(userRepository, times(1)).findByUuid(user.getUuid());
        verify(userRepository, times(1)).delete(user);
    }
}
