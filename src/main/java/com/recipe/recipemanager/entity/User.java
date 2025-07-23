package com.recipe.recipemanager.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "passwordHash", nullable = false)
    private String passwordHash;
    @Column(name = "roles", nullable = false)
    private String roles;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    // This ensures that removing a Recipe from the recipes list will also delete it
    // from the database if it's no longer associated with any User.
    private List<Recipe> recipes;

    public User() {
    }

    public User(String username, String email, String passwordHash, String roles, List<Recipe> recipes) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.recipes = recipes;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", email=" + email + ", passwordHash=" + passwordHash + ", roles=" + roles
                + ", recipes=" + recipes + "]";
    }

}
