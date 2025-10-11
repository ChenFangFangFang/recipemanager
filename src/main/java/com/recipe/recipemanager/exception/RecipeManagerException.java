package com.recipe.recipemanager.exception;


public abstract  class RecipeManagerException extends RuntimeException {
    public RecipeManagerException (String message){
        super(message);
    }

}
