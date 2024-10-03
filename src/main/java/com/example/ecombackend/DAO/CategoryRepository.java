package com.example.ecombackend.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecombackend.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // JpaRepository already provides methods like findAll, findById, save, etc.
}
