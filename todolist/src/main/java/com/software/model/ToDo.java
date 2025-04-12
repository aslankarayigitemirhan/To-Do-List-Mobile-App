package com.software.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "to_do")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "to_do_id")
    private Long todoId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "to_do_tags",
            joinColumns = @JoinColumn(name = "to_do_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
    @Column(name = "to_do_title", nullable = false, length = 100)
    private String todotitle;
    @Column(name = "to_do_detailed_description", nullable = true, length = 400)
    private String todoDetailedDescription;
    @Column(name = "starting_date")
    private LocalDate startingDate;
    @Column(name = "expected_end_time")
    private LocalDate expectedEndTime;
    @Column(name = "tag_priority")
    private Priority tagPriority;

    public ToDo() {
    }

    public ToDo(User owner, String todotitle, Priority tagPriority) {
        this.user = owner;
        this.todotitle = todotitle;
        this.tagPriority = tagPriority;
    }

    public ToDo(User owner, List<Tag> tags, String todotitle, String todoDetailedDescription, LocalDate startingDate, LocalDate expectedEndTime, Priority tagPriority) {
        this.user = owner;
        this.tags = tags;
        this.todotitle = todotitle;
        this.todoDetailedDescription = todoDetailedDescription;
        this.startingDate = startingDate;
        this.expectedEndTime = expectedEndTime;
        this.tagPriority = tagPriority;
    }

    public ToDo(User owner, List<Tag> tags, String todotitle, String todoDetailedDescription, Priority tagPriority) {
        this.user = owner;
        this.tags = tags;
        this.todotitle = todotitle;
        this.todoDetailedDescription = todoDetailedDescription;
        this.tagPriority = tagPriority;
        this.expectedEndTime = null;
        this.startingDate = null;
    }

    public Long getTodoId() {
        return todoId;
    }

    public User getOwner() {
        return user;
    }

    public void setOwner(User owner) {
        this.user = owner;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getTodotitle() {
        return todotitle;
    }

    public void setTodotitle(String todotitle) {
        this.todotitle = todotitle;
    }

    public String getTodoDetailedDescription() {
        return todoDetailedDescription;
    }

    public void setTodoDetailedDescription(String todoDetailedDescription) {
        this.todoDetailedDescription = todoDetailedDescription;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDate getExpectedEndTime() {
        return expectedEndTime;
    }

    public void setExpectedEndTime(LocalDate expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    public Priority getTagPriority() {
        return tagPriority;
    }

    public void setTagPriority(Priority tagPriority) {
        this.tagPriority = tagPriority;
    }
}
