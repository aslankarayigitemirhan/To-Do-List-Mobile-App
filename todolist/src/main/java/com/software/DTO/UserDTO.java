package com.software.DTO;


import java.util.List;

public class UserDTO {
    private String name;
    private String surname;
    private String email;
    private String username;
    private Long userId;

    public UserDTO() {
    }

    public UserDTO(String name, String surname, String email, String username, Long userId) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
