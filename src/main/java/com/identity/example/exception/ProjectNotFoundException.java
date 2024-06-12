package com.identity.example.exception;

import java.util.UUID;
import lombok.Getter;

@Getter
public class ProjectNotFoundException extends RuntimeException {
    private final UUID uuid;
    public ProjectNotFoundException(UUID uuid) {
        super("Project not found with UUID: " + uuid);
        this.uuid = uuid;
    }
}
