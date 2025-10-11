package com.recipe.recipemanager.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.recipe.recipemanager.dto.UserLoginDTO;
import com.recipe.recipemanager.dto.UserResponseDTO;
import com.recipe.recipemanager.dto.UserSignUpDTO;
import com.recipe.recipemanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@Valid @RequestBody UserSignUpDTO userSignUpDTO){
        UserResponseDTO dto = userService.signUp(userSignUpDTO.getEmail(),userSignUpDTO.getPassword(),userSignUpDTO.getConfirmPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response){
    try
        {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDTO.getEmail(),
                            userLoginDTO.getPassword()
                    )
            );
            // Create and set security context
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

            // Save to session using SecurityContextRepository
            securityContextRepository.saveContext(securityContext, request, response);

            System.out.println("=== LOGIN SUCCESS ===");
            System.out.println("Session ID: " + request.getSession().getId());
            System.out.println("Authentication saved: " + authentication.getName());
            System.out.println("===================");
            UserResponseDTO user = userService.getUserByEmail(userLoginDTO.getEmail());
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
    System.out.println("=== LOGIN FAILED ===");
    System.out.println("Exception: " + e.getClass().getName());
    System.out.println("Message: " + e.getMessage());
    throw e;
}


    }
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response){
        System.out.println("\n=== LOGOUT REQUEST RECEIVED ===");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";

        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("Invalidating session: " + session.getId());
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        System.out.println("=== LOGOUT COMPLETE ===\n");
        // Return structured response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Logged out successfully");
        responseBody.put("user", username);
        responseBody.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(responseBody);
    }


//    // Show profile page (after login)
//    @GetMapping("/profile")
//    public String showProfile(Model model, Authentication authentication) {
//        String currentEmail = authentication.getName();
//        Optional<User> user = repository.findByEmail(currentEmail);
//
//        if (user.isPresent()) {
//            model.addAttribute("user", user.get());
//            return "profile"; // Refers to profile.html in the templates folder
//        } else {
//            model.addAttribute("errorMessage", "User not found");
//            return "error";
//        }
//    }
//
//    @GetMapping("/updateprofile")
//    public String showUpdateForm(Model model, Authentication authentication) {
//        String currentEmail = authentication.getName();
//        Optional<User> userOpt = repository.findByEmail(currentEmail);
//
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            ResetPasswordForm form = new ResetPasswordForm();
//            form.setEmail(user.getEmail()); // Set email as read-only
//            form.setUsername(user.getUsername()); // Pre-fill username
//
//            model.addAttribute("resetPasswordForm", form);
//            return "updateProfile";
//        } else {
//            model.addAttribute("errorMessage", "User not found");
//            return "error";
//        }
//    }
//
//    // Process profile update with ResetPasswordForm
//    @PostMapping("/saveprofile")
//    public String updateUser(
//            @Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm form,
//            BindingResult bindingResult,
//            Authentication authentication,
//            Model model) {
//
//        if (bindingResult.hasErrors()) {
//            return "updateProfile";
//        }
//
//        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
//            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
//            return "updateProfile";
//        }
//
//        String currentEmail = authentication.getName();
//        Optional<User> userOpt = repository.findByEmail(currentEmail);
//
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            user.setUsername(form.getUsername());
//            user.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
//            repository.save(user);
//
//            model.addAttribute("user", user);
//            model.addAttribute("successMessage", "Profile updated successfully");
//            return "profile";
//        } else {
//            model.addAttribute("errorMessage", "User not found");
//            return "error";
//        }
//    }
}