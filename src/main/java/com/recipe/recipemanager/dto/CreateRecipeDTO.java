package com.recipe.recipemanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRecipeDTO {
    @NotBlank
    private String title;
    private String description;
    private Long useTimes;
}
