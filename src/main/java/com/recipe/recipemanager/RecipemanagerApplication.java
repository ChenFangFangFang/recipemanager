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
	// private static final Logger log =
	// LoggerFactory.getLogger(RecipemanagerApplication.class);

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
			if (existingTestUser.isEmpty()) {
				User testUser = new User();
				testUser.setUsername("testuser");
				testUser.setEmail("testuser@example.com");
				testUser.setRoles("ROLE_USER");
				testUser.setPasswordHash(passwordEncoder.encode("password123"));
				drepository.save(testUser);
			} else {
				System.out.println("User with email testuser@example.com already exists");
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
		};

	}
}