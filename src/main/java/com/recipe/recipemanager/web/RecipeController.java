package com.recipe.recipemanager.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.recipe.recipemanager.domain.Recipe;
import com.recipe.recipemanager.domain.RecipeRepository;
import com.recipe.recipemanager.domain.User;
import com.recipe.recipemanager.domain.UserRepository;

@Controller
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home() {
        // do something
        return "home";
    }

    @GetMapping("/error")
    public String handleError() {
        // Return a custom error view, or simply return "error" if you have an
        // error.html template
        return "error"; // Ensure there's an error.html in templates
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // This will render the login.html in the templates folder
    }

    // 此处有修改
    @RequestMapping(value = "/{userid}/recipelist")
    public String recipeList(@PathVariable("userid") Long userId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // This will give the email of the logged-in user

        Optional<User> user = userRepository.findByEmail(currentUserEmail);
        if (user.isPresent() && user.get().getId().equals(userId)) {
            List<Recipe> recipes = recipeRepository.findByUserId(userId);
            model.addAttribute("recipes", recipes);
            return "recipes";
        } else {
            return "error";
        }

    }

}