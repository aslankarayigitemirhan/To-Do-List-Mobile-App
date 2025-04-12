package com.software.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToMany(mappedBy = "tags")
    private List<ToDo> toDos;
    @Column(name = "tag_name", nullable = false)
    private String tagName;

    public Tag() {
    }

    public Tag(User owner, String tagName) {
        this.user = owner;
        this.tagName = tagName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ToDo> getToDos() {
        return toDos;
    }

    public void setToDos(List<ToDo> toDos) {
        this.toDos = toDos;
    }

    public Long getTagId() {
        return tagId;
    }

    public User getOwner() {
        return user;
    }

    public void setOwner(User owner) {
        this.user = owner;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
