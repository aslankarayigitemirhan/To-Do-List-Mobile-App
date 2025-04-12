package com.software.repository;

import com.software.model.Tag;
import com.software.model.ToDo;
import com.software.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo,Long> {
    List<ToDo> findByUser(User user); // Matches getOwner() in ToDo
}
