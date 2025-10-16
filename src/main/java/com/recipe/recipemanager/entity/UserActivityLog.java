package com.recipe.recipemanager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_logs")
@Data
@NoArgsConstructor
public class UserActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private Long userId;
    @Column(name = "user_email",nullable = false)
    private String userEmail;
    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public UserActivityLog(Long userId, String userEmail, String activityType, String description) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.activityType = activityType;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
}
