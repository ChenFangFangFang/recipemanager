package com.recipe.recipemanager.repository;

import com.recipe.recipemanager.entity.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {
    List<UserActivityLog> findByUserId(Long id);
    List<UserActivityLog> findByUserEmail(String email);
    List<UserActivityLog> findByActivityType(String activityType);
    List<UserActivityLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<UserActivityLog> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);




}
