package com.software.repository;

import com.software.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Email'e göre kullanıcıyı bulma
    Optional<User> findByEmail(String email);

    // Kullanıcı adıyla kullanıcıyı bulma
    Optional<User> findByUsername(String username);
}
