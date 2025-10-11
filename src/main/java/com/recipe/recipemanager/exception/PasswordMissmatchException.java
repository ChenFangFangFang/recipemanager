package com.recipe.recipemanager.exception;

public class PasswordMissmatchException extends RecipeManagerException{
    public PasswordMissmatchException(){
        super("Passwords do not match");
    }
}
