package com.software.DTO;

import java.time.LocalDate;

public class ToDoCreationRequestWrapper {
    private String title;
    private String description;
    private LocalDate startTime;
    private LocalDate endTime;

//
    public ToDoCreationRequestWrapper() {
    }

    public ToDoCreationRequestWrapper(String title, String description, LocalDate startTime, LocalDate endTime) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }
}
