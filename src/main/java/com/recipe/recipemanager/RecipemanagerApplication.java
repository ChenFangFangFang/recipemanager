package com.recipe.recipemanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.recipe.recipemanager.entity.Tag;
import com.recipe.recipemanager.repository.TagRepository;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class RecipemanagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(RecipemanagerApplication.class, args);
	}

	@Bean
	public CommandLineRunner setupTags(TagRepository tagRepository) {
		return (args) -> {
			if (tagRepository.count() == 0) { // Only insert if there are no tags already
				Tag tag1 = new Tag("Breakfast", new HashSet<>());
				Tag tag2 = new Tag("Lunch", new HashSet<>());
				Tag tag3 = new Tag("Dinner", new HashSet<>());
				Tag tag4 = new Tag("Snacks", new HashSet<>());

				tagRepository.saveAll(Arrays.asList(tag1, tag2, tag3, tag4));
			}
		};
	}

}