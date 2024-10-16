package com.recipe.recipemanager.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.recipe.recipemanager.domain.RecipeRepository;

@Controller
public class RecipeController {
    @Autowired
    private RecipeRepository repository;

    @RequestMapping(value = "/index")
    public String home() {
        // do something
        return "home";
    }

    @RequestMapping(value = "/recipelist")
    public String recipeList(Model model) {
        model.addAttribute("recipes", repository.findAll());
        return "recipes";
    }

}