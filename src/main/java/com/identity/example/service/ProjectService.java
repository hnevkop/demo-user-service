package com.identity.example.service;

import com.identity.example.dto.ProjectDTO;
import com.identity.example.dto.ProjectRequestDTO;

import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(ProjectRequestDTO projectDTO);

    List<ProjectDTO> listProjects();
}
