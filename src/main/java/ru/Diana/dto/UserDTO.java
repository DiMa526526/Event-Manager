package ru.Diana.dto;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String timezone;

    public UserDTO() {}

    public UserDTO(Long id, String username, String email, String timezone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.timezone = timezone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}