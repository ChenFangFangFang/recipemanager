package com.recipe.recipemanager.repository;

import com.recipe.recipemanager.entity.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
