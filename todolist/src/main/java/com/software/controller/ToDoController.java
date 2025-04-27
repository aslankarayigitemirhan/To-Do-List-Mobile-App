package com.software.controller;

import com.software.DTO.ListToDoDTO;
import com.software.DTO.ToDoCreationRequestWrapper;
import com.software.DTO.ToDoDTO;
import com.software.model.Priority;
import com.software.model.User;
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

    //Create List For ToDo
    @PostMapping("/createListForToDos")
    public ResponseEntity<?> createListToDo(@RequestHeader("Authorization") String token, @RequestParam String listName){
        try {
            ListToDoDTO createdToDo = toDoService.createListToDo(token,listName);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdToDo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Spesifik hata mesajı
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteListForAllToDos")
    public ResponseEntity<?> deleteListAndAllTodos(@RequestHeader("Authorization") String token, @RequestParam Long id){
        try {
            ListToDoDTO deleted = toDoService.deleteListAndAllTodos(token,id);
            return ResponseEntity.status(HttpStatus.OK).body(deleted);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Spesifik hata mesajı
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
    @PutMapping("updateTitleOfList")
    public ResponseEntity<?> updateListName(@RequestHeader("Authorization") String token, @RequestParam Long id, @RequestParam String newListName){
        try {
            ListToDoDTO updated = toDoService.updateListName(token,id, newListName);
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Spesifik hata mesajı
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
    // Create To Do
    @PostMapping("/createToDo")
    public ResponseEntity<?> createToDo(@RequestHeader("Authorization") String token,@RequestBody ToDoCreationRequestWrapper newToDo) {
        try {
            ToDoDTO createdToDo = toDoService.createToDo(token,
                    newToDo.getTitle(),
                    newToDo.getDescription(),
                    newToDo.getStartTime(),
                    newToDo.getEndTime(),
                    newToDo.getListId()

            );
            return ResponseEntity.status(HttpStatus.CREATED).body(createdToDo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Spesifik hata mesajı
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    // Delete To Do
    @DeleteMapping("/deleteToDo")
    public ResponseEntity<?> deleteToDo(@RequestHeader("Authorization") String token,@RequestParam Long toDoId ) {
        try {
            String result = toDoService.deleteToDo(toDoId, token);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Update Priority
    @PutMapping("/update-priority")
    public ResponseEntity<?> updatePriority(@RequestHeader("Authorization") String token,@RequestParam Long toDoId, @RequestParam Priority newPriority) {
        try {
            ToDoDTO updatedToDo = toDoService.updatePriority(token,toDoId, newPriority);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Update Dates (Start Date and Expected End Date)
    @PutMapping("/update-dates")
    public ResponseEntity<?> updateDates(@RequestHeader("Authorization") String token,
                                         @RequestParam Long toDoId,
                                         @RequestParam LocalDate newStartDate,
                                         @RequestParam LocalDate newExpectedEndDate) {
        try {
            ToDoDTO updatedToDo = toDoService.updateDates(toDoId, newStartDate, newExpectedEndDate, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Update Title
    @PutMapping("/update-todo-title")
    public ResponseEntity<?> updateTitle(@RequestParam Long toDoId, @RequestParam String newTitle, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.updateTitle(toDoId, newTitle, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Update Description
    @PutMapping("/update-todo-description")
    public ResponseEntity<?> updateDescription(@RequestParam Long toDoId, @RequestParam String newDescription, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.updateDescription(toDoId, newDescription, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo not found or unauthorized");
        }
    }

    // Add Tag
    @PutMapping("/add-tag-to-todo")
    public ResponseEntity<?> addTag(@RequestParam Long toDoId, @RequestParam Long tagId, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.addTag(toDoId, tagId, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo or Tag not found, or unauthorized");
        }
    }

    // Delete Tag
    @DeleteMapping("/delete-tag-from-todo")
    public ResponseEntity<?> deleteTag(@RequestParam Long toDoId, @RequestParam Long tagId, @RequestHeader("Authorization") String token) {
        try {
            ToDoDTO updatedToDo = toDoService.deleteTag(toDoId, tagId, token);
            return ResponseEntity.status(HttpStatus.OK).body(updatedToDo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo or Tag not found, or unauthorized");
        }
    }

    @GetMapping("/getUserAllTodos")
    public ResponseEntity<?> getUserAllTodos(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract token from "Bearer <token>"
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization header missing or invalid");
            }

            // Call UserService to get todos
            List<ToDoDTO> todos = toDoService.getUserAllTodos(authorizationHeader);
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
    @GetMapping("/getUserTodaysTodos")
    public ResponseEntity<?> getUserTodaysTodos(@RequestHeader("Authorization") String token){
        try {
            // Extract token from "Bearer <token>"
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization header missing or invalid");
            }

            // Call UserService to get todos
            List<ToDoDTO> todos = toDoService.getUserTodaysTodos(token);
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
    @GetMapping("getUserLists")
    public ResponseEntity<?> getAllUserList(@RequestHeader("Authorization") String token){
        try {
            // Extract token from "Bearer <token>"
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization header missing or invalid");
            }

            // Call UserService to get todos
            List<ListToDoDTO> todos = toDoService.getAllUserList(token);
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
    @GetMapping("/getSpecificListToDo")
    public ResponseEntity<?> getSpecificListToDoDTO(@RequestHeader("Authorization") String token, Long listId){
        try {
            // Extract token from "Bearer <token>"
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization header missing or invalid");
            }

            // Call UserService to get todos
            List<ToDoDTO> todos = toDoService.getSpecificListToDoDTO(token,listId);
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
