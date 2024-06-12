package com.identity.example.persistence;

import com.identity.example.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.projects WHERE u.uuid = :uuid")
    Optional<User> findByUuidWithProjects(@Param("uuid") UUID uuid);
}
