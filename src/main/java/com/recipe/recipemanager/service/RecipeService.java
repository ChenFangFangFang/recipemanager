package com.recipe.recipemanager.service;

import com.recipe.recipemanager.dto.CreateRecipeDTO;
import com.recipe.recipemanager.dto.RecipeListDTO;
import com.recipe.recipemanager.entity.Recipe;
import com.recipe.recipemanager.entity.Tag;
import com.recipe.recipemanager.entity.User;
import com.recipe.recipemanager.exception.RecipeIsEmptyException;
import com.recipe.recipemanager.exception.RecipeNotFoundException;
import com.recipe.recipemanager.exception.UserNotFoundException;
import com.recipe.recipemanager.repository.RecipeRepository;
import com.recipe.recipemanager.repository.TagRepository;
import com.recipe.recipemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,TagRepository tagRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    public RecipeListDTO convertToDTO(Recipe recipe){
        RecipeListDTO list = new RecipeListDTO();
        list.setUserId(recipe.getUser().getId());
        list.setId(recipe.getId());
        list.setDescription(recipe.getDescription());
        list.setTitle(recipe.getTitle());
        list.setCreatedDate(recipe.getCreatedDate());
        list.setUseTimes(recipe.getUseTimes());
        Set<String> tagNames = recipe.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
        list.setTagName(tagNames);
        return list;
    }
    public RecipeListDTO createRecipe(CreateRecipeDTO recipeDTO){
        Recipe recipe = new Recipe();
        User currentUser = getCurrentUser();
        recipe.setUser(currentUser);
        recipe.setTitle(recipeDTO.getTitle());
        if ( recipeDTO.getDescription() != null){
            recipe.setDescription(recipeDTO.getDescription());
        }
        recipe.setCreatedDate(LocalDateTime.now());
        recipe.setUseTimes(recipeDTO.getUseTimes());
        Set<Tag> tags = processTags(recipeDTO.getTagName());
        recipe.setTags(tags);
        recipeRepository.save(recipe);
        return convertToDTO(recipe);

    }
    public List<RecipeListDTO> getAllListByUser(){
        User currentUser = getCurrentUser();
        return recipeRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    public void deleteRecipe(Long id){
        Recipe recipe = recipeRepository.findById(id).orElseThrow(
                RecipeNotFoundException::new);
        User currentUser = getCurrentUser();
        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own recipes");
        }
        recipeRepository.deleteById(id);

    }
    public RecipeListDTO updateRecipe(Long id, CreateRecipeDTO updateDTO){
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(RecipeNotFoundException::new);
        User currentUser = getCurrentUser();
        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only update your own recipes");
        }
        if (updateDTO.getTitle() != null && !updateDTO.getTitle().isBlank()) {
            recipe.setTitle(updateDTO.getTitle());
        }

        if (updateDTO.getDescription() != null) {
            recipe.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getUseTimes() != null) {
            recipe.setUseTimes(updateDTO.getUseTimes());
        }
        if (updateDTO.getTagName() != null) {
            Set<Tag> tags = processTags(updateDTO.getTagName());
            recipe.setTags(tags);
        }
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return convertToDTO(updatedRecipe);
    }
    public RecipeListDTO incrementUseTimes(Long id){
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
        User currentUser = getCurrentUser();
        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own recipes");
        }
        recipe.setUseTimes(recipe.getUseTimes()+1);
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return convertToDTO(updatedRecipe);
    }
    public RecipeListDTO decrementUseTimes(Long id){
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
        User currentUser = getCurrentUser();
        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own recipes");
        }
        Long currentUseTimes = recipe.getUseTimes();
        if (currentUseTimes > 0) {
            recipe.setUseTimes(currentUseTimes - 1);
        }
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return convertToDTO(updatedRecipe);
    }
    public List<RecipeListDTO> getRecipeByTagName(String tagName){
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }
        User currentUser = getCurrentUser();
        return recipeRepository.findByTagName(tagName.trim())
                .stream()
                .filter(recipe -> recipe.getUser().getId().equals(currentUser.getId()))
                .map(this::convertToDTO)
                .toList();
    }
    public RecipeListDTO getRandomRecipe(){
       User currentUser = getCurrentUser();
        Random random = new Random();
        List<Recipe> recipes = recipeRepository.findByUserId(currentUser.getId());
        if (recipes==null || recipes.isEmpty()){
            throw new RecipeIsEmptyException();
        }
            Recipe randomRecipe = recipes.get(random.nextInt(recipes.size()));
            return convertToDTO(randomRecipe);

    }
    private Set<Tag> processTags(Set<String> tagNames){
        if (tagNames == null || tagNames.isEmpty()) {
            return new HashSet<>();
        }
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tag = tagRepository.save(tag);
            }

            tags.add(tag);
        }
        return tags;
    }
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(email);
        }
        return userRepository.findByEmail(email);
    }

}
