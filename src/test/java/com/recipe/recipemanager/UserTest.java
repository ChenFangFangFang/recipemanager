package com.recipe.recipemanager;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.recipe.recipemanager.entity.User;
import com.recipe.recipemanager.repository.UserRepository;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmail() {
        Optional<User> user = userRepository.findByEmail("2@1.com");
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo("2@1.com");

    }

}
