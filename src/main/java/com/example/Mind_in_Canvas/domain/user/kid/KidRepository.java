package com.example.Mind_in_Canvas.domain.user.kid;

import com.example.Mind_in_Canvas.domain.user.parent.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KidRepository extends JpaRepository<Kid, UUID> {
    Kid findByKidId(UUID kidId);
    User findParentByKidId(UUID kidId);

    @Query("SELECT k FROM Kid k WHERE k.parent.email = :email")
    List<Kid> findAllByParentEmail(String email);
}
