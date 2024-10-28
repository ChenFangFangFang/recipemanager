package com.recipe.recipemanager.web;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.recipe.recipemanager.domain.Recipe;
import com.recipe.recipemanager.domain.RecipeRepository;
import com.recipe.recipemanager.domain.Tag;
import com.recipe.recipemanager.domain.TagRepository;
import com.recipe.recipemanager.domain.User;
import com.recipe.recipemanager.domain.UserRepository;

@Controller
public class RecipeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/recipelist")
    public String recipeList(Model model) {
        String currentUserEmail = getCurrentUserEmail();
        Optional<User> user = userRepository.findByEmail(currentUserEmail);
        if (user.isPresent()) {
            List<Recipe> recipes = recipeRepository.findByUserId(user.get().getId());
            model.addAttribute("recipes", recipes);
            return "recipes";
        } else {
            return "error";
        }
    }

    @GetMapping("/addrecipe")
    public String addRecipe(Model model) {
        model.addAttribute("tags", tagRepository.findAll());
        model.addAttribute("recipeForm", new Recipe());
        return "addRecipe";
    }

    @RequestMapping(value = "/saverecipe", method = RequestMethod.POST)
    public String saveRecipe(@ModelAttribute("recipeForm") Recipe recipe, Model model,
            @RequestParam("tags") List<Long> tagIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        Optional<User> currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser.isPresent()) {
            recipe.setUser(currentUser.get()); // Assign the current user to the recipe
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }
        Set<Tag> tags = new HashSet<>();
        for (Long tagId : tagIds) {
            tagRepository.findById(tagId).ifPresent(tags::add); // Fetch each tag by ID and add it to the set
        }
        recipe.setTags(tags); // Set the tags to the recipe
        recipe.setCreatedDate(LocalDateTime.now());

        // Save the recipe
        recipeRepository.save(recipe);

        return "redirect:/recipelist";
    }

    @RequestMapping(value = "/editrecipe/{id}", method = RequestMethod.GET)
    public String editRecipe(@PathVariable("id") Long recipeId, Model model) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        System.out.println("Recipe ID: " + recipeId);

        if (recipe.isPresent()) {
            Recipe recipeData = recipe.get();
            Set<Tag> tags = recipeData.getTags();
            if (tags != null) {
                System.out.println("Tags size: " + tags.size());
                for (Tag tag : tags) {
                    System.out.println("Tag: " + tag.getName());
                }
            }

            model.addAttribute("tags", tagRepository.findAll());
            model.addAttribute("recipeEditForm", recipe.get());
            return "editRecipe"; // Load the edit recipe page
        } else {
            model.addAttribute("errorMessage", "Recipe not found");
            return "error";
        }
    }

    @RequestMapping(value = "/updaterecipe", method = RequestMethod.POST)
    public String updateRecipe(@RequestParam("id") Long recipeId,
            @ModelAttribute("recipeEditForm") Recipe recipeForm,
            @RequestParam("tags") List<Long> tagIds,
            Model model) {
        System.out.println("Updating Recipe ID: " + recipeId);
        System.out.println("Updated Title: " + recipeForm.getTitle());
        Optional<Recipe> exstingRecipeOpt = recipeRepository.findById(recipeId);

        if (exstingRecipeOpt.isPresent()) {
            Recipe existingRecipe = exstingRecipeOpt.get();
            existingRecipe.setTitle(recipeForm.getTitle());
            existingRecipe.setDescription(recipeForm.getDescription());
            existingRecipe.setUseTimes(recipeForm.getUseTimes());
            Set<Tag> tags = new HashSet<>();
            for (Long tagId : tagIds) {
                tagRepository.findById(tagId).ifPresent(tags::add);
            }
            existingRecipe.setTags(tags);
            recipeRepository.save(existingRecipe);
            System.out.println("Recipe updated successfully in database.");

        } else {
            model.addAttribute("errorMessage", "recipe not found");
            return "error";
        }
        return "redirect:/recipelist"; // Redirect to the recipe list after update
    }

    // RESTful service to get recipe by id
    @RequestMapping(value = "/recipe/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Recipe> findRecipesRest(@PathVariable("id") Long recipeId) {
        return recipeRepository.findById(recipeId);

    }

    @RequestMapping(value = "/deleterecipe/{id}", method = RequestMethod.POST)
    public String deleteRecipe(@PathVariable("id") Long recipeId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Fetch the recipe by ID
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isPresent()) {
            Recipe recipe = recipeOptional.get();

            // Check if the logged-in user is the owner of the recipe
            if (recipe.getUser().getEmail().equals(currentUserEmail)) {
                recipeRepository.delete(recipe);
                return "redirect:/recipelist";
            } else {
                model.addAttribute("errorMessage", "You are not authorized to delete this recipe.");
                return "error";
            }
        } else {
            model.addAttribute("errorMessage", "Recipe not found.");
            return "error";
        }
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/randomrecipe")
    @ResponseBody
    public ResponseEntity<Recipe> getRandomRecipe() {
        String currentUserEmail = getCurrentUserEmail();
        Optional<User> user = userRepository.findByEmail(currentUserEmail);
        if (user.isPresent()) {
            List<Recipe> recipes = recipeRepository.findByUserId(user.get().getId());
            if (!recipes.isEmpty()) {
                Random random = new Random();
                Recipe randomRecipe = recipes.get(random.nextInt(recipes.size()));
                return ResponseEntity.ok(randomRecipe);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
        } else {
            // Handle case where user is not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
    }

}