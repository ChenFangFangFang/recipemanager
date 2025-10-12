package com.recipe.recipemanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CreateRecipeDTO {
    @NotBlank(message = "The title of the recipe is required")
    @Size(min = 4)
    private String title;
    private String description;
    @Min(value = 0, message = "Use times cannot be negative")
    private Long useTimes = 1L;
    private Set<String> tagName;
}
