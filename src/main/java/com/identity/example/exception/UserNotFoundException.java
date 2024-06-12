package com.identity.example.exception;


import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final UUID uuid;

    public UserNotFoundException(UUID uuid) {
        super("User not found with UUID: " + uuid);
        this.uuid = uuid;
    }

}
