package com.software.controller;

import com.software.DTO.ToDoCreationRequestWrapper;
import com.software.DTO.ToDoDTO;
import com.software.model.Priority;
import com.software.model.Tag;
import com.software.model.ToDo;
import com.software.service.ToDoService;
import com.software.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {

    private final ToDoService toDoService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ToDoController(ToDoService toDoService, JwtUtil jwtUtil) {
        this.toDoService = toDoService;
        this.jwtUtil = jwtUtil;
    }

    // Create To Do
    @PostMapping("/create")
    public ResponseEntity<?> createToDo(@RequestBody ToDoCreationRequestWrapper newToDo, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO createdToDo = toDoService.createToDo(
                    newToDo.getTitle(),
                    newToDo.getDescription(),
                    newToDo.getStartTime(),
                    newToDo.getEndTime(),
                    token
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdToDo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Spesifik hata mesajı
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    // Delete To Do
    @DeleteMapping("/delete/{toDoId}")
    public ResponseEntity<?> deleteToDo(@PathVariable Long toDoId, @RequestHeader("Authorization") String token) {
        try {
            String result = toDoService.deleteToDo(toDoId, token);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Update Priority
    @PutMapping("/update-priority/{toDoId}")
    public ResponseEntity<?> updatePriority(@PathVariable Long toDoId, @RequestParam Priority newPriority, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.updatePriority(toDoId, newPriority, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Update Dates (Start Date and Expected End Date)
    @PutMapping("/update-dates/{toDoId}")
    public ResponseEntity<?> updateDates(@PathVariable Long toDoId,
                                         @RequestParam LocalDate newStartDate,
                                         @RequestParam LocalDate newExpectedEndDate,
                                         @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.updateDates(toDoId, newStartDate, newExpectedEndDate, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Update Title
    @PutMapping("/update-title/{toDoId}")
    public ResponseEntity<?> updateTitle(@PathVariable Long toDoId, @RequestParam String newTitle, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.updateTitle(toDoId, newTitle, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Update Description
    @PutMapping("/update-description/{toDoId}")
    public ResponseEntity<?> updateDescription(@PathVariable Long toDoId, @RequestParam String newDescription, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.updateDescription(toDoId, newDescription, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Add Tag
    @PutMapping("/add-tag/{toDoId}")
    public ResponseEntity<?> addTag(@PathVariable Long toDoId, @RequestParam Long tagId, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.addTag(toDoId, tagId, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo or Tag not found, or unauthorized");
        }
    }

    // Delete Tag
    @DeleteMapping("/delete-tag/{toDoId}")
    public ResponseEntity<?> deleteTag(@PathVariable Long toDoId, @RequestParam Long tagId, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.deleteTag(toDoId, tagId, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo or Tag not found, or unauthorized");
        }
    }

    @GetMapping("/getTodos")
    public ResponseEntity<?> getUserTodos(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract token from "Bearer <token>"
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization header missing or invalid");
            }
            String token = authorizationHeader.replace("Bearer ", ""); // Remove "Bearer " prefix

            // Call UserService to get todos
            List<ToDoDTO> todos = toDoService.getUserTodos(token);
            return ResponseEntity.ok(todos);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found: " + e.getMessage());
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
}
