package com.example.ecombackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecombackend.model.Category;
import com.example.ecombackend.service.ApiResponse;
import com.example.ecombackend.service.CategoryService;
import com.example.ecombackend.service.ObjResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

	
    @Autowired
    private CategoryService categoryService;
//    @GetMapping("/categories")
//    public ResponseEntity<ObjResponse<List<Category>>> getAllCategories(
//            @RequestParam(defaultValue = "1") int page, 
//            @RequestParam(defaultValue = "10") int size) {
//
//        ObjResponse<List<Category>> response = categoryService.getAllCategories(page, size);
//        return ResponseEntity.ok(response);
//    }
@GetMapping("/countCategories")
public ApiResponse count() {
	return new ApiResponse(true,String.valueOf(categoryService.countCategories()));
}
    // Endpoint to get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        ObjResponse<List<Category>> response = categoryService.getAllCategories();
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getData());  // Success case
        } else {
            return ResponseEntity.ok(response.getData());  // Failure case
        }
    }

    // Endpoint to get a specific category by ID
    @GetMapping("/{id}")
    public ResponseEntity<ObjResponse<Category>> getCategoryById(@PathVariable Long id) {
        ObjResponse<Category> response = categoryService.getCategoryById(id);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);  // Success case
        } else {
            return ResponseEntity.status(404).body(response);  // Failure case
        }
    }
}
