package com.recipe.recipemanager;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.recipe.recipemanager.domain.Tag;
import com.recipe.recipemanager.domain.TagRepository;

import java.util.List;

@SpringBootTest
public class TagTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void testFindByName() {
        List<Tag> foundTags = tagRepository.findByName("Dinner");
        assertThat(foundTags).hasSize(1);
        assertThat(foundTags.get(0).getName()).isEqualTo("Dinner");
        assertThat(foundTags.get(0).getId()).isNotNull();
    }
}
