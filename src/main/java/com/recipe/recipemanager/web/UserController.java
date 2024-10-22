package com.recipe.recipemanager.web;

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

@Controller
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login"; // This will render the login.html in the templates folder
    }

    @RequestMapping(value = "signup", method = RequestMethod.GET)
    public String addUser(Model model) {
        model.addAttribute("signupform", new SignupForm());
        return "signup";
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

}
