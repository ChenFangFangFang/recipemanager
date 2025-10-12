package com.recipe.recipemanager.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class RecipeListDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private Long useTimes;
    private Long userId;
    private Set<String> tagName;
}
