package com.recipe.recipemanager.repository;

import com.recipe.recipemanager.entity.Recipe;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    List<Recipe> findByTitle(String title);

    List<Recipe> findByUserId(Long userId);

    List<Recipe> findByTags_Name(String tagName);
}
