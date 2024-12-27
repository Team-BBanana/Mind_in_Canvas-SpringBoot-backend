package com.example.Mind_in_Canvas.domain.user;

import com.example.Mind_in_Canvas.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "parent")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Parent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parentId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Setter
    @Column(nullable = true, columnDefinition = "INT(6) ZEROFILL")
    private String pin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Parent(String username, String email, String name, Role role) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public void updateUserInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
