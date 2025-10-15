package com.recipe.recipemanager.exception;

public class RecipeIsEmptyException extends  RecipeManagerException{
    public RecipeIsEmptyException(){
        super("You don't have any recipe yet");
    }
}
