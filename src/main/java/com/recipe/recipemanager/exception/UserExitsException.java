package com.recipe.recipemanager.exception;

public class UserExitsException extends RecipeManagerException{
    public UserExitsException(String email){
        super("User with email " + email + " already exists");
    }
}
