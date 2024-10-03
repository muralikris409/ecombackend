package com.example.ecombackend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ecombackend.DAO.CategoryRepository;
import com.example.ecombackend.model.Category;
import com.example.ecombackend.service.ObjResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Method to get all categories wrapped in ObjResponse
    public ObjResponse<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (!categories.isEmpty()) {
            return new ObjResponse<>(true, "Categories fetched successfully", categories);
        } else {
            return new ObjResponse<>(false, "No categories found", null);
        }
    }

    public long countCategories() {
    	return categoryRepository.count();
    }

    // Method to get all categories with pagination
    public ObjResponse<List<Category>> getAllCategories(int page, int size) {
        Pageable paging = PageRequest.of(page - 1, size);  // Page starts from 0
        List<Category> categories = categoryRepository.findAll(paging).getContent();
        return new ObjResponse<>(!categories.isEmpty(), "Categories fetched successfully", categories);
    }
    // Method to get a category by ID, wrapped in ObjResponse
    public ObjResponse<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> new ObjResponse<>(true, "Category found", category))
                .orElseGet(() -> new ObjResponse<>(false, "Category not found", null));
    }
}
