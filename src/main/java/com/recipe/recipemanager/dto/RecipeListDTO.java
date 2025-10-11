package com.recipe.recipemanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecipeListDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private Long useTimes;
    private String userEmail;
    private Long userId;
}
