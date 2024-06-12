package com.identity.example.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.identity.example.dto.ProjectDTO;
import com.identity.example.dto.ProjectRequestDTO;
import com.identity.example.model.Project;
import com.identity.example.persistence.ProjectRepository;
import com.identity.example.service.ProjectMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

  @Mock private ProjectRepository projectRepository;

  @Mock private ProjectMapper projectMapper;

  @InjectMocks private ProjectServiceImpl projectService;

  private Project project;
  private ProjectDTO projectDTO;
  private ProjectRequestDTO projectRequestDTO;

  @BeforeEach
  void setUp() {
    var projectUuid = UUID.randomUUID();

    project = new Project();
    project.setName("project");
    project.setUuid(projectUuid);

    projectDTO = new ProjectDTO();
    projectDTO.setName("project");
    projectDTO.setUuid(projectUuid);

    projectRequestDTO = new ProjectRequestDTO();
    projectRequestDTO.setName("project");
  }

  @Test
  void shouldCreateProject() {
    when(projectMapper.projectDtoToProject(any(ProjectRequestDTO.class))).thenReturn(project);
    when(projectRepository.save(project)).thenReturn(project);
    when(projectMapper.projectToProjectDto(project)).thenReturn(projectDTO);

    var createdProject = projectService.createProject(projectRequestDTO);

    assertNotNull(createdProject);
    assertEquals(project.getUuid(), createdProject.getUuid());
    assertEquals(project.getName(), createdProject.getName());
    verify(projectRepository, times(1)).save(project);
  }

  @Test
  void shouldListProjects() {
    List<Project> projects = List.of(project);
    when(projectRepository.findAll()).thenReturn(projects);
    when(projectMapper.projectToProjectDto(project)).thenReturn(projectDTO);

    var retrievedProjects = projectService.listProjects();

    assertNotNull(retrievedProjects);
    assertFalse(retrievedProjects.isEmpty());
    assertEquals(1, retrievedProjects.size());
    assertEquals(project.getUuid(), retrievedProjects.get(0).getUuid());
    assertEquals(project.getName(), retrievedProjects.get(0).getName());
  }
}
