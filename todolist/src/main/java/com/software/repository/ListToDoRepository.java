package com.software.repository;

import com.software.model.ListToDo;
import com.software.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListToDoRepository extends JpaRepository<ListToDo,Long> {
    List<ListToDo> findByUser(User user);
}
