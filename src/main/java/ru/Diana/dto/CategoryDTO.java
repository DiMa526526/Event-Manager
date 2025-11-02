package ru.Diana.dto;

public class CategoryDTO {
    private Long id;
    private String name;
    private String color;
    private String description;
    private UserDTO user;

    public CategoryDTO() {}

    public CategoryDTO(Long id, String name, String color, String description, UserDTO user) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
}