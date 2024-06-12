package com.identity.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequestDTO {
  @NotNull(message = "Name cannot be null")
  @Size(min = 1, max = 120, message = "Name cannot be empty")
  private String name;

  private Boolean external;
}
