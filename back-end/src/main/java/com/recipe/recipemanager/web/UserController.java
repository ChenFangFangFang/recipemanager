package com.recipe.recipemanager.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String addUser(Model model) {
        model.addAttribute("signupform", new SignupForm());
        return "signup";
    }

    @RequestMapping(value = "saveuser", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("signupform", signupForm);
            return "signup";
        }
        if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
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
        } else {
            bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords doee not match");
            return "signup";
        }

        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        String currentEmail = authentication.getName();
        Optional<User> user = repository.findByEmail(currentEmail);

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "profile"; // Refers to the profile.html file in the templates folder
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }
    }

    @RequestMapping(value = "/updateprofile", method = RequestMethod.GET)
    public String showUpdateForm(Model model, Authentication authentication) {
        String currentEmail = authentication.getName();
        Optional<User> userOpt = repository.findByEmail(currentEmail);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            return "updateProfile";
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "error";
        }
    }

    @RequestMapping(value = "/saveprofile", method = RequestMethod.POST)
    public String updateUser(
            @RequestParam String newUsername,
            @RequestParam String newPassword,
            Authentication authentication,
            Model model) {
        String currentEmail = authentication.getName();
        Optional<User> userOpt = repository.findByEmail(currentEmail);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(newUsername);
            user.setPasswordHash(passwordEncoder.encode(newPassword));
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
