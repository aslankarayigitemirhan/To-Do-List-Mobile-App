package com.software.DTO;

import java.util.List;

public class ListToDoDTO {
    private Long listId;
    private List<ToDoDTO> alltodos;
    private String listName;
    private UserDTO userDTO;

    public ListToDoDTO() {
    }

    public ListToDoDTO(Long listId, List<ToDoDTO> alltodos, String listName, UserDTO userDTO) {
        this.listId = listId;
        this.alltodos = alltodos;
        this.listName = listName;
        this.userDTO = userDTO;
    }

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listToDoDTOId) {
        this.listId = listToDoDTOId;
    }

    public List<ToDoDTO> getAlltodos() {
        return alltodos;
    }

    public void setAlltodos(List<ToDoDTO> alltodos) {
        this.alltodos = alltodos;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
