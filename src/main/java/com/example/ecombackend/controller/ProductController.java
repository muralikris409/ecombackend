package com.example.ecombackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecombackend.model.Product;
import com.example.ecombackend.service.ProductService;
import com.example.ecombackend.service.ApiResponse;
import com.example.ecombackend.service.ObjResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all products
    @GetMapping("/products")
    public ResponseEntity<ObjResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        ObjResponse<List<Product>> response = new ObjResponse(true, "Sucessfully fetched records",products);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    

  

 
}
