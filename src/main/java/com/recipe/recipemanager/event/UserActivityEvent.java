package com.recipe.recipemanager.event;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;


public class UserActivityEvent extends ApplicationEvent {
    private final Long userId;
    private final String email;
    private final LocalDateTime occurredAt;
    private final ActivityType activityType;

    @Getter
    public enum ActivityType {
        REGISTRATION("User registration"),
        LOGIN("User login"),
        LOGOUT("User logout"),
        PASSWORD_RESET("Reset password");
        private final String description;

        ActivityType(String description) {
            this.description = description;
        }

    }
    public UserActivityEvent(Object source, Long userId, String email,ActivityType activityType){
        super(source);
        this.userId = userId;
        this.email = email;
        this.activityType = activityType;
        this.occurredAt = LocalDateTime.now();
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    @Override
    public String toString() {
        return "UserActivityEvent{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", registeredAt=" + occurredAt +
                ", activityType=" + activityType +
                '}';
    }
}
