package com.recipe.recipemanager.service;

import com.recipe.recipemanager.dto.UserResponseDTO;
import com.recipe.recipemanager.exception.InvalidCredentialsException;
import com.recipe.recipemanager.exception.PasswordMissmatchException;
import com.recipe.recipemanager.exception.UserExitsException;
import com.recipe.recipemanager.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.recipe.recipemanager.entity.User;
import com.recipe.recipemanager.repository.UserRepository;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(email);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }
    public UserResponseDTO convertToDTO(User user){
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setEmail(user.getEmail());
        return responseDTO;
    }
    public UserResponseDTO signUp(String email, String password, String confirmPassword){
        if (email== null || password == null || confirmPassword==null || email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
           throw new IllegalArgumentException("The email or password should not be empty");
        }
        if(userRepository.existsByEmail(email)){
            throw new UserExitsException(email);
        }
        if (!password.equals(confirmPassword)){
            throw new PasswordMissmatchException();
        }
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);
        user.setRole(User.Role.USER);
        User saved = userRepository.save(user);
        return convertToDTO(saved);
    }
    public UserResponseDTO login(String email, String password) {
        if (email== null || password == null || email.isBlank() || password.isBlank()){
            throw new IllegalArgumentException("The email or password should not be empty");
        }
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserNotFoundException(email);
        }
        if (!passwordEncoder.matches(password,user.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        return convertToDTO(user);
    }
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserNotFoundException(email);
        }

        return convertToDTO(user);
    }
    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    public UserResponseDTO resetPassword(String email, String password, String confirmPassword){
        if (email== null || password == null || confirmPassword==null || email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
            throw new IllegalArgumentException("The email or password should not be empty");
        }
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException(email);
        }
        if (!password.equals(confirmPassword)){
            throw new PasswordMissmatchException();
        }
        String hashedPassword = passwordEncoder.encode(password);
        user.setPasswordHash(hashedPassword);
        User saved = userRepository.save(user);
        return convertToDTO(saved);
    }



}
