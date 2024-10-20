package com.recipe.recipemanager.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.recipe.recipemanager.domain.Recipe;
import com.recipe.recipemanager.domain.RecipeRepository;
import com.recipe.recipemanager.domain.SignupForm;
import com.recipe.recipemanager.domain.User;
import com.recipe.recipemanager.domain.UserRepository;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String addUser(Model model) {
        model.addAttribute("signupform", new SignupForm());
        return "signup";
    }

    @GetMapping("/recipelist")
    public String recipeList(Model model) {
        String currentUserEmail = getCurrentUserEmail();
        Optional<User> user = repository.findByEmail(currentUserEmail);
        if (user.isPresent()) {
            List<Recipe> recipes = recipeRepository.findByUserId(user.get().getId());
            model.addAttribute("recipes", recipes);
            return "recipes";
        } else {
            return "error";
        }
    }

    /**
     * Create new user
     * Check if user already exists & form validation
     * 
     * @param signupForm
     * @param bindingResult
     * @return
     */

    @RequestMapping(value = "saveuser", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
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
        } else {
            return "signup";
        }

        return "redirect:/login";
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
