package com.software.service;

import com.software.DTO.TagDTO;
import com.software.DTO.ToDoDTO;
import com.software.DTO.UserDTO;
import com.software.model.User;
import com.software.repository.UserRepository;
import com.software.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities
        );
    }
    public User getUser(Long id){
        return this.userRepository.findById(id).get();
    }
    public User getUserByUsername(String username) {

        User user = null;

        for(User user1 : this.userRepository.findAll()){
            if(user1.getUsername().equals(username)){
                user = user1;
                break;
            }
        }
        return user;
    }

    @Transactional
    public UserDTO registerUser(String name, String surname, String email, String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(name, surname, email, username, encodedPassword);

        try {
            User savedUser = userRepository.save(user);
            return new UserDTO(savedUser.getName(), savedUser.getSurname(), savedUser.getEmail(), savedUser.getUsername(),user.getOwnerId());
        } catch (Exception e) {
            throw new RuntimeException("Error during registration: " + e.getMessage());
        }
    }

    @Transactional
    public String deleteUser(String username) {
        User user = getUserByUsername(username);
        userRepository.delete(user);
        return "User " + username + " deleted successfully.";
    }

    @Transactional
    public UserDTO updateEmail(String username, String newEmail) {
        User user = getUserByUsername(username);
        user.setEmail(newEmail);
        userRepository.save(user);
        return new UserDTO(user.getName(), user.getSurname(), newEmail, user.getUsername(),user.getOwnerId());
    }

    @Transactional
    public String updatePassword(String username, String currentPassword, String newPassword) {
        User user = getUserByUsername(username);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password updated successfully";
    }

    @Transactional
    public String updateUsername(String oldUsername, String newUsername) {
        User user = getUserByUsername(oldUsername);
        if (userRepository.findByUsername(newUsername).isPresent()) {
            throw new RuntimeException("New username is already taken");
        }
        user.setUsername(newUsername);
        userRepository.save(user);
        String newToken = this.jwtUtil.generateToken(newUsername);
        return newToken;
    }

    @Transactional
    public UserDTO updateNameAndSurname(String username, String newName, String newSurname) {
        User user = getUserByUsername(username);
        user.setName(newName);
        user.setSurname(newSurname);
        userRepository.save(user);
        return new UserDTO(newName, newSurname, user.getEmail(), user.getUsername(),user.getOwnerId());
    }
    @Transactional
    public List<TagDTO> getUserTags(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        UserDTO userDTO = new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getOwnerId());

        return user.getTagsBelonging().stream()
                .map(tag -> new TagDTO(
                        tag.getTagId(),
                        userDTO,
                        tag.getToDos().stream()
                                .map(todo -> new ToDoDTO(todo.getTodoId(), todo.getTodotitle(), todo.getTodoDetailedDescription(),todo.getStartingDate(),todo.getExpectedEndTime(),userDTO)) // Adjust ToDoDTO fields
                                .collect(Collectors.toList()),
                        tag.getTagName()
                ))
                .collect(Collectors.toList());
    }
}