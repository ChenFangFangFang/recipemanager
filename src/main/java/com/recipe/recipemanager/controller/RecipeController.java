package com.recipe.recipemanager.controller;

import com.recipe.recipemanager.dto.CreateRecipeDTO;
import com.recipe.recipemanager.dto.RecipeListDTO;
import com.recipe.recipemanager.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }
    @PostMapping
    public ResponseEntity<RecipeListDTO> createNewRecipe(@Valid @RequestBody CreateRecipeDTO dto) {
        RecipeListDTO recipe = recipeService.createRecipe(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe);
    }
    @GetMapping
    public ResponseEntity<List<RecipeListDTO>> getAllRecipe(){
        List<RecipeListDTO> recipes = recipeService.getAllListByUser();
        return ResponseEntity.ok(recipes);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id){
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<RecipeListDTO> updateRecipe(@PathVariable Long id, @Valid @RequestBody CreateRecipeDTO dto){
        RecipeListDTO updatedRecipe = recipeService.updateRecipe(id,dto);
        return ResponseEntity.ok(updatedRecipe);
    }
    @PostMapping("/{id}/increment-usage")
    public ResponseEntity<RecipeListDTO> increaseUsage(@PathVariable Long id){
        RecipeListDTO updatedRecipe = recipeService.incrementUseTimes(id);
        return ResponseEntity.ok(updatedRecipe);
    }
    @PostMapping("/{id}/decrement-usage")
    public ResponseEntity<RecipeListDTO> decreaseUsage(@PathVariable Long id){
        RecipeListDTO updatedRecipe = recipeService.decrementUseTimes(id);
        return ResponseEntity.ok(updatedRecipe);
    }
    @GetMapping("/my/by-tag")
    public ResponseEntity<List<RecipeListDTO>> getMyRecipesByTag(
            @RequestParam String tagName) {
        List<RecipeListDTO> recipes = recipeService.getRecipeByTagName(tagName);
        return ResponseEntity.ok(recipes);
    }
    @GetMapping("/random")
    public ResponseEntity<RecipeListDTO> getRandomRecipe(){
        RecipeListDTO recipe = recipeService.getRandomRecipe();
        return ResponseEntity.ok(recipe);
    }

}
