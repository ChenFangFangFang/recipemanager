package com.recipe.recipemanager.repository;

import com.recipe.recipemanager.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByUserId(Long userId);
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.tags t WHERE t.name = :tagName")
    List<Recipe> findByTagName(@Param("tagName") String tagName);

}
