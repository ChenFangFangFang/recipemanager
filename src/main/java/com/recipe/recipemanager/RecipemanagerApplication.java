package com.recipe.recipemanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.recipe.recipemanager.domain.Recipe;
import com.recipe.recipemanager.domain.RecipeRepository;
import com.recipe.recipemanager.domain.Tag;
import com.recipe.recipemanager.domain.TagRepository;
import com.recipe.recipemanager.domain.User;
import com.recipe.recipemanager.domain.UserRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class RecipemanagerApplication {
	// private static final Logger log =
	// LoggerFactory.getLogger(RecipemanagerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RecipemanagerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(RecipeRepository repository, TagRepository tagRepository,
			UserRepository drepository) {
		return (args) -> {
			Tag tag1 = new Tag("Breakfast", new HashSet<>());
			tagRepository.save(tag1);
			User user1 = new User();
			user1.setUsername("chen");
			user1.setEmail("1@qq.com");
			user1.setRoles("user");
			user1.setPasswordHash("$2y$10$mbVzVg7IzFqI5025oH05z.sshSXIbJdajGJxdlGtVzzaNVRZly5Bi");
			drepository.save(user1);

			Set<Tag> tags = new HashSet<>();
			tags.add(tag1); // You can add more tags if needed

			Recipe recipe1 = new Recipe(
					"Bread",
					"a newest bread",
					LocalDateTime.of(2024, 1, 1, 0, 0),
					5,
					tags, user1);
			repository.save(recipe1);
		};

	}
}