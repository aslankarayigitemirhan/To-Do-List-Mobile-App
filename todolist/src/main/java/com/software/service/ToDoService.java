package com.software.service;

import com.software.DTO.ListToDoDTO;
import com.software.DTO.ToDoDTO;
import com.software.DTO.UserDTO;
import com.software.model.*;
import com.software.repository.ListToDoRepository;
import com.software.repository.TagRepository;
import com.software.repository.ToDoRepository;
import com.software.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TagRepository tagRepository;
    private final ListToDoRepository listToDoRepository;

    @Transactional
    public ListToDoDTO createListToDo(String token, String listName){
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        ListToDo listToDo = new ListToDo(listName,user);
        this.listToDoRepository.save(listToDo);
        UserDTO userDTO = new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getOwnerId());
        return new ListToDoDTO(listToDo.getListToDoId(),null,listName,userDTO);
    }
    @Transactional
    public List<ToDoDTO> getSpecificListToDoDTO(String token,Long id){
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);
        List<ListToDo> allToDosList = user.getListToDo();
        for(ListToDo l: allToDosList){
            if(l.getListToDoId() == id){
                return conversionAllToDoDTO(l);
            }
        }return null;
    }
    private List<ToDoDTO> conversionAllToDoDTO(ListToDo listToDo){
        List<ToDoDTO> returningList = new ArrayList<>();
        for(ToDo todo: listToDo.getListOfToDos()){
            returningList.add(convertToToDoDTO(todo));
        }
        return returningList;
    }
    @Transactional
    public ListToDoDTO deleteListAndAllTodos(String token, Long id){
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getOwnerId());
        ListToDoDTO returningList = new ListToDoDTO(id, conversionAllToDoDTO(this.listToDoRepository.findById(id) .orElseThrow(() -> new RuntimeException("List Id is not found for this user"))),this.listToDoRepository.findById(id) .orElseThrow(() -> new RuntimeException("List Id is not found for this user")).getListName(),userDTO);
        this.listToDoRepository.deleteById(id);
        return returningList;
    }
    @Transactional
    public ListToDoDTO updateListName(String token, Long id, String newListName){
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getOwnerId());
        ListToDo listToDo = this.listToDoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("List Id is not found for this user"));
        listToDo.setListName(newListName);
        this.listToDoRepository.save(listToDo);
        return new ListToDoDTO(listToDo.getListToDoId(),conversionAllToDoDTO(listToDo),listToDo.getListName(),userDTO);
    }
    public List<ListToDoDTO> getAllUserList(String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getOwnerId());
        List<ListToDo> toDoLists = listToDoRepository.findByUser(user);
        List<ListToDoDTO> returningList = toDoLists.stream()
                .map(toDoList -> new ListToDoDTO(toDoList.getListToDoId(),conversionAllToDoDTO(toDoList),toDoList.getListName(),userDTO))
                .collect(Collectors.toList());

        return returningList;
    }
/*
* ListToDoService :
* - createList(...)
* - deleteList(...)
* - updateListName(...)
* -
* -
*
*
* 
* */
    @Autowired
    public ToDoService(ListToDoRepository listToDoRepository,ToDoRepository toDoRepository, UserService userService, JwtUtil jwtUtil, TagRepository tagRepository) {
        this.toDoRepository = toDoRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.tagRepository = tagRepository;
        this.listToDoRepository = listToDoRepository;
    }

    private ToDoDTO convertToToDoDTO(ToDo toDo) {
        User owner = toDo.getOwner();
        UserDTO userDTO = new UserDTO(owner.getName(), owner.getSurname(), owner.getEmail(), owner.getUsername(), owner.getOwnerId());
        return new ToDoDTO(
                toDo.getTodoId(),
                toDo.getTodotitle(),
                toDo.getTodoDetailedDescription(),
                toDo.getStartingDate(),
                toDo.getExpectedEndTime(),
                toDo.getTagPriority(),
                userDTO
        );
    }

    // Create To Do
    public ToDoDTO createToDo(String token, String title, String description, LocalDate startDate, LocalDate expectedEndDate,Long ListId) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);
        ListToDo listToDo = this.listToDoRepository.findById(ListId)
                .orElseThrow(() -> new RuntimeException("List Id is not found for this user"));
        ToDo newToDo = new ToDo(listToDo, user, null,title, description, startDate, expectedEndDate,null);
        ToDo savedToDo = toDoRepository.save(newToDo);
        return convertToToDoDTO(savedToDo);
    }

    // Delete To Do
    public String deleteToDo(Long toDoId, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Optional<ToDo> optionalToDo = toDoRepository.findById(toDoId);
        if (optionalToDo.isEmpty()) {
            throw new RuntimeException("ToDo not found");
        }

        ToDo toDo = optionalToDo.get();
        if (!toDo.getOwner().equals(user)) {
            throw new RuntimeException("You are not authorized to delete this ToDo");
        }

        toDoRepository.delete(toDo);
        return "ToDo deleted successfully.";
    }

    // Update To Do's Priority
    public ToDoDTO updatePriority(String token,Long toDoId, Priority newPriority) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Optional<ToDo> optionalToDo = toDoRepository.findById(toDoId);
        if (optionalToDo.isEmpty()) {
            throw new RuntimeException("ToDo not found");
        }

        ToDo toDo = optionalToDo.get();
        if (!toDo.getOwner().equals(user)) {
            throw new RuntimeException("You are not authorized to update this ToDo");
        }

        toDo.setTagPriority(newPriority);
        ToDo updatedToDo = toDoRepository.save(toDo);

        return convertToToDoDTO(updatedToDo);
    }

    // Revise LocalDate (Starting Date, Expected End Time)
    public ToDoDTO updateDates(Long toDoId, LocalDate newStartDate, LocalDate newExpectedEndDate, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Optional<ToDo> optionalToDo = toDoRepository.findById(toDoId);
        if (optionalToDo.isEmpty()) {
            throw new RuntimeException("ToDo not found");
        }

        ToDo toDo = optionalToDo.get();
        if (!toDo.getOwner().equals(user)) {
            throw new RuntimeException("You are not authorized to update this ToDo");
        }

        toDo.setStartingDate(newStartDate);
        toDo.setExpectedEndTime(newExpectedEndDate);
        ToDo updatedToDo = toDoRepository.save(toDo);

        return convertToToDoDTO(updatedToDo);
    }

    // Update Title
    public ToDoDTO updateTitle(Long toDoId, String newTitle, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Optional<ToDo> optionalToDo = toDoRepository.findById(toDoId);
        if (optionalToDo.isEmpty()) {
            throw new RuntimeException("ToDo not found");
        }

        ToDo toDo = optionalToDo.get();
        if (!toDo.getOwner().equals(user)) {
            throw new RuntimeException("You are not authorized to update this ToDo");
        }

        toDo.setTodotitle(newTitle);
        ToDo updatedToDo = toDoRepository.save(toDo);

        return convertToToDoDTO(updatedToDo);
    }

    // Update Description
    public ToDoDTO updateDescription(Long toDoId, String newDescription, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Optional<ToDo> optionalToDo = toDoRepository.findById(toDoId);
        if (optionalToDo.isEmpty()) {
            throw new RuntimeException("ToDo not found");
        }

        ToDo toDo = optionalToDo.get();
        if (!toDo.getOwner().equals(user)) {
            throw new RuntimeException("You are not authorized to update this ToDo");
        }

        toDo.setTodoDetailedDescription(newDescription);
        ToDo updatedToDo = toDoRepository.save(toDo);

        return convertToToDoDTO(updatedToDo);
    }

    // Add Tag
    public ToDoDTO addTag(Long toDoId, Long tagId, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Optional<ToDo> optionalToDo = toDoRepository.findById(toDoId);
        if (optionalToDo.isEmpty()) {
            throw new RuntimeException("ToDo not found");
        }

        ToDo toDo = optionalToDo.get();
        if (!toDo.getOwner().equals(user)) {
            throw new RuntimeException("You are not authorized to update this ToDo");
        }

        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if (optionalTag.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }

        Tag tag = optionalTag.get();
        if (!tag.getUser().equals(user)) {
            throw new RuntimeException("You are not authorized to use this Tag");
        }

        toDo.getTags().add(tag);
        ToDo updatedToDo = toDoRepository.save(toDo);

        return convertToToDoDTO(updatedToDo);
    }
    // Delete Tag
    public ToDoDTO deleteTag(Long toDoId, Long tagId, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Optional<ToDo> optionalToDo = toDoRepository.findById(toDoId);
        if (optionalToDo.isEmpty()) {
            throw new RuntimeException("ToDo not found");
        }

        ToDo toDo = optionalToDo.get();
        if (!toDo.getOwner().equals(user)) {
            throw new RuntimeException("You are not authorized to update this ToDo");
        }

        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if (optionalTag.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }

        Tag tag = optionalTag.get();
        if (!tag.getUser().equals(user)) {
            throw new RuntimeException("You are not authorized to modify this Tag");
        }

        toDo.getTags().remove(tag);
        ToDo updatedToDo = toDoRepository.save(toDo);

        return convertToToDoDTO(updatedToDo);
    }
    @Transactional
    public List<ToDoDTO> getUserAllTodos(String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getOwnerId());

        return toDoRepository.findByUser(user).stream()
                .map(todo -> new ToDoDTO(
                        todo.getTodoId(),
                        todo.getTodotitle(),
                        todo.getTodoDetailedDescription(),
                        todo.getStartingDate(),
                        todo.getExpectedEndTime(),
                        todo.getTagPriority(),
                        userDTO
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public List<ToDoDTO> getUserTodaysTodos(String token) {
        // JWT token doğrulama
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }

        User user = userService.getUserByUsername(username);
        UserDTO userDTO = new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getOwnerId());

        LocalDate today = LocalDate.now();

        return toDoRepository.findByUser(user).stream()
                .filter(todo -> todo.getExpectedEndTime() != null) // null olanları es geç
                .filter(todo -> todo.getExpectedEndTime().isEqual(today)) // bugüne ait olanlar
                .map(todo -> new ToDoDTO(
                        todo.getTodoId(),
                        todo.getTodotitle(),
                        todo.getTodoDetailedDescription(),
                        todo.getStartingDate(),
                        todo.getExpectedEndTime(),
                        todo.getTagPriority(),
                        userDTO
                ))
                .collect(Collectors.toList());
    }

}