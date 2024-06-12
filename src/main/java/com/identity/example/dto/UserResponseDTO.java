package com.identity.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;


@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private UUID uuid;
    private String email;
    private String name;
}
