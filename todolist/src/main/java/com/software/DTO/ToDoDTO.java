package com.software.DTO;

import java.time.LocalDate;

public class ToDoDTO {
    private Long todoId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private UserDTO owner; // User yerine UserDTO kullanıyoruz

    public ToDoDTO() {
    }

    public ToDoDTO(Long todoId, String title, String description, LocalDate startDate, LocalDate expectedEndDate, UserDTO owner) {
        this.todoId = todoId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.owner = owner;
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(LocalDate expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }
}