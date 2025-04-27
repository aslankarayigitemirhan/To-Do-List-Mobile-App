package com.software.service;

import com.software.DTO.TagDTO;
import com.software.DTO.ToDoDTO;
import com.software.DTO.UserDTO;
import com.software.model.Tag;
import com.software.model.ToDo;
import com.software.model.User;
import com.software.repository.TagRepository;
import com.software.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

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
                toDo.getTagPriority(),
                userDTO
        );
    }

    // Yardımcı metod: Tag’i TagDTO’ya çevirir
    private TagDTO convertToTagDTO(Tag tag) {
        User user = tag.getUser();
        UserDTO userDTO = new UserDTO(user.getName(), user.getSurname(), user.getEmail(), user.getUsername(), user.getOwnerId());
        List<ToDoDTO> toDoDTOs = tag.getToDos() != null
                ? tag.getToDos().stream()
                .map(this::convertToToDoDTO)
                .collect(Collectors.toList())
                : null;
        return new TagDTO(tag.getTagId(), userDTO, toDoDTOs, tag.getTagName());
    }

    // Create Tag
    public TagDTO createTag(String tagName, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Tag newTag = new Tag();
        newTag.setTagName(tagName);
        newTag.setUser(user);
        Tag savedTag = tagRepository.save(newTag);

        return convertToTagDTO(savedTag);
    }

    // Delete Tag
    public String deleteTag(Long tagId, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if (optionalTag.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }

        Tag tag = optionalTag.get();
        if (!tag.getUser().equals(user)) {
            throw new RuntimeException("You are not authorized to delete this Tag");
        }

        tagRepository.delete(tag);
        return "Tag deleted successfully.";
    }

    // Update Tag Name
    public TagDTO updateTagName(Long tagId, String newTagName, String token) {
        String jwtToken = token.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(jwtToken);
        if (!jwtUtil.validateToken(jwtToken, username)) {
            throw new RuntimeException("Invalid token");
        }
        User user = userService.getUserByUsername(username);

        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        if (optionalTag.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }

        Tag tag = optionalTag.get();
        if (!tag.getUser().equals(user)) {
            throw new RuntimeException("You are not authorized to update this Tag");
        }

        tag.setTagName(newTagName);
        Tag updatedTag = tagRepository.save(tag);

        return convertToTagDTO(updatedTag);
    }
}