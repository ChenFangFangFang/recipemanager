package com.recipe.recipemanager.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.recipe.recipemanager.domain.ForgotPasswordForm;
import com.recipe.recipemanager.domain.ResetPasswordForm;
import com.recipe.recipemanager.domain.SignupForm;
import com.recipe.recipemanager.domain.User;
import com.recipe.recipemanager.domain.UserRepository;

import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Display the login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Display the signup form
    @GetMapping("/signup")
    public String addUser(Model model) {
        model.addAttribute("signupform", new SignupForm());
        return "signup";
    }

    // Save new user after signup
    @PostMapping("/saveuser")
    public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("signupform", signupForm);
            return "signup";
        }

        if (!signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords do not match");
            return "signup";
        }

        String pwd = signupForm.getPassword();
        String hashPwd = passwordEncoder.encode(pwd);

        User newUser = new User();
        newUser.setPasswordHash(hashPwd);
        newUser.setUsername(signupForm.getUsername());
        newUser.setEmail(signupForm.getEmail());
        newUser.setRoles("USER");

        if (!repository.findByEmail(signupForm.getEmail()).isPresent()) {
            repository.save(newUser);
        } else {
            bindingResult.rejectValue("email", "err.email", "Email already exists");
            return "signup";
        }

        return "redirect:/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("forgotPasswordForm", new ForgotPasswordForm());
        return "forgot-password"; // Ensure this matches the template name
    }

    // Process forgot password form submission
    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @Valid @ModelAttribute("forgotPasswordForm") ForgotPasswordForm form,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "forgot-password"; // Return to form if validation errors are present
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
            return "forgot-password";
        }

        Optional<User> userOpt = repository.findByEmail(form.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPasswordHash(passwordEncoder.encode(form.getNewPassword())); // Encode and set new password
            repository.save(user); // Save the updated user

            model.addAttribute("successMessage", "Your password has been reset successfully.");
            return "login"; // Redirect to login page after successful reset
        } else {
            bindingResult.rejectValue("email", "error.email", "No account found with this email");
            return "forgot-password";
        }
    }

    // Show profile page (after login)
    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        String currentEmail = authentication.getName();
        Optional<User> user = repository.findByEmail(currentEmail);

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "profile"; // Refers to profile.html in the templates folder
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }
    }

    @GetMapping("/updateprofile")
    public String showUpdateForm(Model model, Authentication authentication) {
        String currentEmail = authentication.getName();
        Optional<User> userOpt = repository.findByEmail(currentEmail);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            ResetPasswordForm form = new ResetPasswordForm();
            form.setEmail(user.getEmail()); // Set email as read-only
            form.setUsername(user.getUsername()); // Pre-fill username

            model.addAttribute("resetPasswordForm", form);
            return "updateProfile";
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }
    }

    // Process profile update with ResetPasswordForm
    @PostMapping("/saveprofile")
    public String updateUser(
            @Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "updateProfile";
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
            return "updateProfile";
        }

        String currentEmail = authentication.getName();
        Optional<User> userOpt = repository.findByEmail(currentEmail);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(form.getUsername());
            user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
            repository.save(user);

            model.addAttribute("user", user);
            model.addAttribute("successMessage", "Profile updated successfully");
            return "profile";
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }
    }
}