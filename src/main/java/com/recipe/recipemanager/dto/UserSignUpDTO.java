package com.recipe.recipemanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignUpDTO {

    @NotBlank(message = "The email shouldn't be empty")
    @Email(message = "Please enter a valid email")
    private String email;
    @NotBlank(message = "The password shouldn't be empty")
    @Size(min = 6, max = 50, message = "The length of the password should between 6 and 50")
    private String password;
    @NotBlank(message = "The password shouldn't be empty")
    @Size(min = 6, max = 50, message = "The length of the password should between 6 and 50")
    private String confirmPassword;

}
