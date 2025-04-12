package com.software.DTO;
import java.util.List;

public class TagDTO {
    private Long tagId;
    private UserDTO user; // Tag’in sahibi
    private List<ToDoDTO> toDos; // Tag ile ilişkili ToDo’lar
    private String tagName;

    public TagDTO() {
    }

    public TagDTO(Long tagId, UserDTO user, List<ToDoDTO> toDos, String tagName) {
        this.tagId = tagId;
        this.user = user;
        this.toDos = toDos;
        this.tagName = tagName;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<ToDoDTO> getToDos() {
        return toDos;
    }

    public void setToDos(List<ToDoDTO> toDos) {
        this.toDos = toDos;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}