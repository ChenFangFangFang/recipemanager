package com.recipe.recipemanager.exception;

public class RecipeNotFoundException extends RecipeManagerException {
    public RecipeNotFoundException(){
        super("The recipe is not found");
    }
}
