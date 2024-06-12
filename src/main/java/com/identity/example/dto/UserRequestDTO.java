package com.identity.example.dto;

import jakarta.validation.constraints.Email;
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
public class UserRequestDTO {
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 120, message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Password cannot be null")
    @Size(min = 1,  max = 120, message = "Password cannot be empty")
    private String password;
}
