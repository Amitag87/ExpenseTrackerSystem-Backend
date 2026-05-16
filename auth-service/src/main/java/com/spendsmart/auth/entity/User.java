package com.spendsmart.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String passwordHash;

    private String currency = "INR";

    private String timezone = "Asia/Kolkata";

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private Provider provider = Provider.LOCAL;

    private Boolean isActive = true;

    private Double monthlyBudget;

    private String bio;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public enum Provider {
        LOCAL, GOOGLE
    }
}