package com.recipe.recipemanager.event.listener;

import com.recipe.recipemanager.entity.UserActivityLog;
import com.recipe.recipemanager.event.UserActivityEvent;
import com.recipe.recipemanager.repository.UserActivityLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class UserActivityListener {
    private static final Logger logger = LoggerFactory.getLogger(UserActivityListener.class);
    private final UserActivityLogRepository activityLogRepository;
    public UserActivityListener(UserActivityLogRepository activityLogRepository){
        this.activityLogRepository  = activityLogRepository;
    }
    @EventListener
    @Async
    @Transactional
    public void handleUserActivity(UserActivityEvent event){
        try{
            logger.info("User registration received: userId={}, userEmail={}, type={}",event.getUserId(),event.getEmail(),event.getActivityType());
            UserActivityLog log = new UserActivityLog(
                    event.getUserId(),
                    event.getEmail(),
                    event.getActivityType().name(),
                    String.format("Log successfully, time at:%s", event.getOccurredAt())
            );
            activityLogRepository.save(log);
            logger.info("User registration saved: userId={}, userEmail={}, type={}", event.getUserId(),event.getEmail(),event.getActivityType());

        }catch (Exception e){
            logger.error("User log saved failed: userId={}, userEmail={},  type={}, error={}",
                    event.getUserId(), event.getEmail(), event.getActivityType(), e.getMessage(), e);

        }
    }
    private String buildDescription(UserActivityEvent event) {
        StringBuilder desc = new StringBuilder();
        desc.append(event.getActivityType().getDescription());
        desc.append(", time: ").append(event.getOccurredAt());
        return desc.toString();
    }
}
