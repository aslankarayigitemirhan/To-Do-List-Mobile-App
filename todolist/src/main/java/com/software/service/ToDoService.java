package com.software.service;

import com.software.DTO.TagDTO;
import com.software.DTO.ToDoDTO;
import com.software.DTO.UserDTO;
import com.software.model.Priority;
import com.software.model.Tag;
import com.software.model.ToDo;
import com.software.model.User;
import com.software.repository.TagRepository;
import com.software.repository.ToDoRepository;
import com.software.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TagRepository tagRepository;

    @Autowired
    public ToDoService(ToDoRepository toDoRepository, UserService userService, JwtUtil jwtUtil, TagRepository tagRepository) {
        this.toDoRepository = toDoRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.tagRepository = tagRepository;
    }

    // Yardımcı metod: ToDo’yu ToDoDTO’ya çevirir
    private ToDoDTO convertToToDoDTO(ToDo toDo) {
        User owner = toDo.getOwner();
        UserDTO userDTO = new UserDTO(owner.getName(), owner.getSurname(), owner.getEmail(), owner.getUsername(), owner.getOwnerId());
        return new ToDoDTO(
                toDo.getTodoId(),
                toDo.getTodotitle(),
                toDo.getTodoDetailedDescription(),
                toDo.getStartingDate(),
                toDo.getExpectedEndTime(),
                userDTO
        );
    }

    // Create To Do
    public ToDoDTO createToDo(String title, String description, LocalDate startDate, LocalDate expectedEndDate, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        ToDo newToDo = new ToDo(user, null, title, description, startDate, expectedEndDate, null);
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
    public ToDoDTO updatePriority(Long toDoId, Priority newPriority, String token) {
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
    public List<ToDoDTO> getUserTodos(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getOwnerId());

        return toDoRepository.findByUser(user).stream()
                .map(todo -> new ToDoDTO(
                        todo.getTodoId(),
                        todo.getTodotitle(),
                        todo.getTodoDetailedDescription(),
                        todo.getStartingDate(),
                        todo.getExpectedEndTime(),
                        userDTO
                ))
                .collect(Collectors.toList());
    }
}