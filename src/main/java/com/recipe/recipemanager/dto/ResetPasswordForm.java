package com.recipe.recipemanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ResetPasswordForm {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 5, max = 30)
    private String username;

    @NotEmpty
    @Size(min = 7, max = 30)
    private String newPassword;

    @NotEmpty
    @Size(min = 7, max = 30)
    private String confirmPassword;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
