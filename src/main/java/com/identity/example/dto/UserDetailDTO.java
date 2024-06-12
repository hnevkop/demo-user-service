package com.identity.example.dto;

import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDTO {
    private UUID uuid;
    private String email;
    private String name;
    private Set<ProjectDTO> projects;
}
