package com.identity.example.service;

import com.identity.example.dto.ProjectDTO;
import com.identity.example.dto.ProjectRequestDTO;
import com.identity.example.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProjectMapper {

    
    public abstract ProjectDTO projectToProjectDto(Project project);

    public abstract Project projectDtoToProject(ProjectRequestDTO projectDto);
    
}
