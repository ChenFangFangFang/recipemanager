package com.recipe.recipemanager.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "app_user")
public class User {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "passwordHash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    // This ensures that removing a Recipe from the recipes list will also delete it
    // from the database if it's no longer associated with any User.
    private List<Recipe> recipes;

    public User(String email, String passwordHash) {

        this.email = email;
        this.passwordHash = passwordHash;

    }
    @Override
    public String toString() {
        return "User [email=" + email + "]";
    }
    public enum Role {
        USER,
        ADMIN,
        MODERATOR
    }
}
