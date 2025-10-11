package com.recipe.recipemanager.exception;

public class InvalidCredentialsException extends RecipeManagerException{
    public InvalidCredentialsException(){
        super("Invalid email or password");
    }
}
