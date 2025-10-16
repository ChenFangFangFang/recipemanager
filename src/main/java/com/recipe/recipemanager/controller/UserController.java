package com.recipe.recipemanager.controller;

import java.util.List;

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
            );SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            securityContextRepository.saveContext(securityContext, request, response);
            userService.recordLoginActivity(userLoginDTO.getEmail());
            UserResponseDTO user = userService.getUserByEmail(userLoginDTO.getEmail());
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
    System.out.println("=== LOGIN FAILED ===");
    System.out.println("Exception: " + e.getClass().getName());
    System.out.println("Message: " + e.getMessage());
    throw e;
}
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            userService.recordLogoutActivity(email);
            SecurityContextHolder.clearContext();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            return ResponseEntity.ok("Logout successfully");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No login");

    }
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }
}