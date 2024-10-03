package com.example.ecombackend.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecombackend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

}

