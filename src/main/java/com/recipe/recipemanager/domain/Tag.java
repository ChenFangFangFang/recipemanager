package com.recipe.recipemanager.domain;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Recipe> recipes = new HashSet<>();

    public Tag() {
    }

    public Tag(String name, Set<Recipe> recipes) {
        this.name = name;
        this.recipes = recipes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public String toString() {
        return "Tag [name=" + name + ", recipes=" + recipes + "]";
    }

}
