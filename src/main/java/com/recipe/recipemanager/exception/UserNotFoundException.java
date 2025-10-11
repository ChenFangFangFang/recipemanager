package com.recipe.recipemanager.exception;

public class UserNotFoundException extends RecipeManagerException{
    public UserNotFoundException(String email){
        super("User with email " + email + " not found");
    }
}
