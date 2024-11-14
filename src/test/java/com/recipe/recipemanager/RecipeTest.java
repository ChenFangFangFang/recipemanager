package com.recipe.recipemanager;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.recipe.recipemanager.domain.Recipe;
import com.recipe.recipemanager.domain.RecipeRepository;
import com.recipe.recipemanager.domain.TagRepository;
import com.recipe.recipemanager.domain.UserRepository;

@SpringBootTest
public class RecipeTest {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByTitleShouldReturnRecipe() {
        List<Recipe> recipes = recipeRepository.findByTitle("title2 - new");
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getTitle()).isEqualTo("title2 - new");
    }

}
