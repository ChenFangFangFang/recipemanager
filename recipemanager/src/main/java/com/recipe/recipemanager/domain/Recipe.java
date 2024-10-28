package com.recipe.recipemanager.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime createdDate;
    // private List<LocalDateTime> useDates; maybe do it in the next iteration
    private int useTimes;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    // merge is totally ok, not others!!!!
    @JoinTable(name = "recipe_tags", joinColumns = @JoinColumn(name = "recipe_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    // @ManyToMany(cascade = CascadeType.MERGE) // Ensure tags are attached before
    // persisting recipe
    // private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Recipe() {

    }

    public Recipe(String title, String description, LocalDateTime createdDate,
            int useTimes, Set<Tag> tags, User user) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.useTimes = useTimes;
        this.tags = tags;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(int useTimes) {
        this.useTimes = useTimes;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Recipe [title=" + title + ", description=" + description + ", createdDate=" + createdDate
                + ", useTimes=" + useTimes + ", tags=" + tags + "]";
    }

}
