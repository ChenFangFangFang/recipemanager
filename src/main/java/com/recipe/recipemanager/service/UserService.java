package com.recipe.recipemanager.service;

import com.recipe.recipemanager.dto.UserResponseDTO;
import com.recipe.recipemanager.event.UserActivityEvent;
import com.recipe.recipemanager.exception.InvalidCredentialsException;
import com.recipe.recipemanager.exception.PasswordMissmatchException;
import com.recipe.recipemanager.exception.UserExitsException;
import com.recipe.recipemanager.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.recipe.recipemanager.entity.User;
import com.recipe.recipemanager.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
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

        UserActivityEvent event = new UserActivityEvent(
                this,
                saved.getId(),
                saved.getEmail(),
                UserActivityEvent.ActivityType.REGISTRATION
        );
        eventPublisher.publishEvent(event);

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
        UserActivityEvent event = new UserActivityEvent(
                this,
                user.getId(),
                user.getEmail(),
                UserActivityEvent.ActivityType.LOGIN
                );
        eventPublisher.publishEvent(event);
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
        UserActivityEvent event = new UserActivityEvent(
                this,
                    user.getId(),
        user.getEmail(),
        UserActivityEvent.ActivityType.PASSWORD_RESET);
        eventPublisher.publishEvent(event);
        return convertToDTO(saved);
    }

    @Transactional
    public void recordLoginActivity(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(email);
        }

        logUserActivity(user.getId(), user.getEmail(), UserActivityEvent.ActivityType.LOGIN);
    }
    @Transactional
    public void recordLogoutActivity(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(email);
        }

        logUserActivity(user.getId(), user.getEmail(), UserActivityEvent.ActivityType.LOGOUT);
    }
    private void logUserActivity(Long userId, String email, UserActivityEvent.ActivityType activityType) {
        UserActivityEvent event = new UserActivityEvent(
                this,
                userId,
                email,
                activityType
        );
        eventPublisher.publishEvent(event);
    }


}
