package com.recipe.recipemanager.repository;

import com.recipe.recipemanager.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByUserId(Long userId);

}
