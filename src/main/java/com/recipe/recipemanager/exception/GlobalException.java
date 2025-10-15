package com.recipe.recipemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;


@RestControllerAdvice
public class GlobalException {
    // Add this test handler at the top
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception e) {
        System.out.println("=== GLOBAL EXCEPTION HANDLER TRIGGERED ===");
        System.out.println("Exception: " + e.getClass().getName());
        System.out.println("Message: " + e.getMessage());

        return ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred")
                .title("Error")
                .detail(e.getMessage())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleBadCredentials(BadCredentialsException e) {
        return ErrorResponse.builder(e, HttpStatus.UNAUTHORIZED, "Invalid email or password")
                .title("Authentication Failed")
                .detail("The email or password you entered is incorrect")
                .build();
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResponse handleUsernameNotFound(UsernameNotFoundException e) {
        return ErrorResponse.builder(e, HttpStatus.UNAUTHORIZED, "Invalid email or password")
                .title("Authentication Failed")
                .detail("The email or password you entered is incorrect")
                .build();
    }
    @ExceptionHandler(UserExitsException.class)
    public ErrorResponse handleUserExits(UserExitsException e){
        return ErrorResponse.builder(e, HttpStatus.CONFLICT,e.getMessage())
                .title("User exits")
                .detail(e.getMessage())
                .build();
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ErrorResponse handleUserInvalidCredentials(InvalidCredentialsException e){
        return ErrorResponse.builder(e,HttpStatus.UNAUTHORIZED,e.getMessage())
                .title("The email or password is not correct")
                .detail(e.getMessage())
                .build();
    }
    @ExceptionHandler(PasswordMissmatchException.class)
    public ErrorResponse handlePasswordMissMatch(PasswordMissmatchException e){
        return ErrorResponse.builder(e,HttpStatus.BAD_REQUEST,e.getMessage())
                .title("The passwords are not match")
                .detail(e.getMessage())
                .build();
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(UserNotFoundException e){
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND, e.getMessage())
                .title("The user is not found")
                .detail(e.getMessage())
                .build();
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException e) {
        return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, e.getMessage())
                .title("Invalid Input")
                .detail(e.getMessage())
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException e) {
        return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, "Validation failed")
                .title("Validation Error")
                .detail("Please check your input data")
                .build();
    }
    @ExceptionHandler(RecipeNotFoundException.class)
    public ErrorResponse handleRecipeNotFound(RecipeNotFoundException e){
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND,e.getMessage())
                .title("The recipe is not found")
                .detail(e.getMessage())
                .build();
    }
    @ExceptionHandler(RecipeIsEmptyException.class)
    public ErrorResponse RecipeIsEmptyException(RecipeIsEmptyException e){
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND,e.getMessage())
                .title("No recipe")
                .detail(e.getMessage())
                .build();
    }

}
