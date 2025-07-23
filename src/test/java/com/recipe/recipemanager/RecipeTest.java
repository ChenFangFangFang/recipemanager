package com.recipe.recipemanager;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.recipe.recipemanager.entity.Recipe;
import com.recipe.recipemanager.repository.RecipeRepository;
import com.recipe.recipemanager.repository.TagRepository;
import com.recipe.recipemanager.repository.UserRepository;

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
        List<Recipe> recipes = recipeRepository.findByTitle("title4 - new");
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getTitle()).isEqualTo("title4 - new");
    }

}
