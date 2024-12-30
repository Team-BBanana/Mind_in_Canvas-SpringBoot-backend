package com.example.Mind_in_Canvas.domain.user.kid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KidRepository extends JpaRepository<Kid, UUID> {
    Kid findByKidId(UUID kidId);
}
