package com.example.ecombackend.controller;

import com.example.ecombackend.service.ElasticProductService;
import com.example.ecombackend.model.ProductIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping
public class ProductSearchController {

    @Autowired
    private ElasticProductService productSearchService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/auth/search")
    public List<ProductIndex> searchProducts(@RequestParam String query) {
        return productSearchService.searchProducts(query);
    }

    @GetMapping("/search")
    public List<ProductIndex> searchAllProducts(@RequestParam String searchTerm) {
        return productSearchService.searchAllFields(searchTerm);
    }
    
}