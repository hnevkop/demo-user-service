package com.identity.example.service.impl;

import com.identity.example.dto.ProjectDTO;
import com.identity.example.dto.ProjectRequestDTO;
import com.identity.example.model.Project;
import com.identity.example.persistence.ProjectRepository;
import com.identity.example.service.ProjectMapper;
import com.identity.example.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
  private final ProjectRepository projectRepository;
  private final ProjectMapper projectMapper;

  public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
    this.projectRepository = projectRepository;
    this.projectMapper = projectMapper;
  }

  @Override
  public ProjectDTO createProject(ProjectRequestDTO projectRequestDTO) {
    Project project = projectMapper.projectDtoToProject(projectRequestDTO);
    return projectMapper.projectToProjectDto(projectRepository.save(project));
  }

  @Override
  public List<ProjectDTO> listProjects() {
    return projectRepository.findAll().stream().map(projectMapper::projectToProjectDto).toList();
  }
}
