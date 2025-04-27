package com.software.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "list_to_do_for_users")
public class ListToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id")
    private Long listToDoId;
    @Column(name = "list_name")
    private String listName;
    @OneToMany(mappedBy = "todoBelongingAsList",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ToDo> listOfToDos;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ListToDo() {
    }

    public ListToDo(String listName, List<ToDo> listOfToDos, User user) {
        this.listName = listName;
        this.listOfToDos = listOfToDos;
        this.user = user;
    }

    public Long getListToDoId() {
        return listToDoId;
    }

    public void setListToDoId(Long listToDoId) {
        this.listToDoId = listToDoId;
    }

    public ListToDo(String listName, User user) {
        this.listName = listName;
        this.user = user;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public List<ToDo> getListOfToDos() {
        return listOfToDos;
    }

    public void setListOfToDos(List<ToDo> listOfToDos) {
        this.listOfToDos = listOfToDos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
