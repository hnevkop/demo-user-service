package com.identity.example.controller;

import com.identity.example.dto.ProjectDTO;
import com.identity.example.dto.ProjectRequestDTO;
import com.identity.example.service.ProjectService;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    ProjectDTO createProject(@Validated @RequestBody ProjectRequestDTO projectRequestDTO) {
        return projectService.createProject(projectRequestDTO);
    }

    @GetMapping
    List<ProjectDTO> getAllProjects() {
        return projectService.listProjects();
    }

}
