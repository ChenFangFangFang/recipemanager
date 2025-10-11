package com.recipe.recipemanager.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private Long useTimes;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(name = "recipe_tags", joinColumns = @JoinColumn(name = "recipe_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Recipe(String title, String description, LocalDateTime createdDate,
                  Long useTimes, Set<Tag> tags, User user) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.useTimes = useTimes;
        this.tags = tags;
        this.user = user;
    }


    @Override
    public String toString() {
        return "Recipe [title=" + title + ", description=" + description + ", createdDate=" + createdDate
                + ", useTimes=" + useTimes + "]";
    }

}
