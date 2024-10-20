package com.recipe.recipemanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.recipe.recipemanager.domain.Recipe;
import com.recipe.recipemanager.domain.RecipeRepository;
import com.recipe.recipemanager.domain.Tag;
import com.recipe.recipemanager.domain.TagRepository;
import com.recipe.recipemanager.domain.User;
import com.recipe.recipemanager.domain.UserRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class RecipemanagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(RecipemanagerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(RecipeRepository repository, TagRepository tagRepository,
			UserRepository drepository, BCryptPasswordEncoder passwordEncoder) {
		return (args) -> {
			Tag tag1 = new Tag("Breakfast", new HashSet<>());
			tagRepository.save(tag1);

			Optional<User> existingUser = drepository.findByEmail("1@qq.com");
			User user1;
			if (existingUser.isEmpty()) {
				user1 = new User();
				user1.setUsername("newchen");
				user1.setEmail("1@qq.com");
				user1.setRoles("ROLE_USER");
				// Hash the password and set it
				user1.setPasswordHash(passwordEncoder.encode("chen"));
				drepository.save(user1);
			} else {
				user1 = existingUser.get();
			}

			Optional<User> existingTestUser = drepository.findByEmail("testuser@example.com");
			User user2;
			if (existingTestUser.isEmpty()) {
				user2 = new User();
				user2.setUsername("testuser");
				user2.setEmail("testuser@example.com");
				user2.setRoles("ROLE_USER");
				user2.setPasswordHash(passwordEncoder.encode("password123"));
				drepository.save(user2);
			} else {
				user2 = existingTestUser.get();
			}
			Set<Tag> tags = new HashSet<>();
			tags.add(tag1); // You can add more tags if needed

			Recipe recipe1 = new Recipe(
					"New Bread",
					"a newest bread",
					LocalDateTime.of(2024, 1, 1, 0, 0),
					5,
					tags, user1);
			repository.save(recipe1);
			Recipe recipe2 = new Recipe(
					"User 2New Bread",
					"a newest bread",
					LocalDateTime.of(2024, 1, 1, 0, 0),
					5,
					tags, user2);
			repository.save(recipe2);
		};

	}
}