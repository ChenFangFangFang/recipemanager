package com.recipe.recipemanager.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.recipe.recipemanager.domain.RecipeRepository;

@Controller
public class RecipeController {
    @Autowired
    private RecipeRepository repository;

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

    @RequestMapping(value = "/recipelist")
    public String recipeList(Model model) {
        model.addAttribute("recipes", repository.findAll());
        return "recipes";
    }

}