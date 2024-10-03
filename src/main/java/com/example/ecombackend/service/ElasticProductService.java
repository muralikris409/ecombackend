package com.example.ecombackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecombackend.DAO.ProductRepository;
import com.example.ecombackend.model.Product;
import com.example.ecombackend.model.ProductIndex;
import com.example.ecombackend.repository.elastic.ElasticProductRepository;

import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticProductService {

    @Autowired
    private ElasticProductRepository productRepository;

    @Autowired
    private ProductRepository mysqlProductRepository;

    @Transactional
    public void syncProductsToElasticSearch() {
        // Fetch all products from MySQL
        List<Product> products = mysqlProductRepository.findAll();

        // Convert and save each product to ElasticSearch
        for (Product product : products) {
        	  ProductIndex productIndex = new ProductIndex();
              productIndex.setId(product.getId());
              productIndex.setName(product.getName());
              productIndex.setDescription(product.getDescription());
              productIndex.setMrp(product.getMrp());
              productIndex.setDiscount(product.getDiscount());
              productIndex.setStock(product.getStock());
              // Set category if needed
              productIndex.setCategory(product.getCategory().getCategory()); // Assuming getName() returns the category name

              productRepository.save(productIndex);
        }
    }
    @PostConstruct
    public void init() {
    	syncProductsToElasticSearch();
    }
    public List<ProductIndex> searchProducts(String query){
    	return productRepository.findByNameContaining(query);
    }
    public List<ProductIndex> getAllProductsFromElastic() {
        return (List<ProductIndex>) productRepository.findAll();
    }
    
    public List<ProductIndex> searchAllFields(String searchTerm) {
        BigDecimal priceLimit = null;

        // Check if the search term contains "under" followed by a number (for price filtering)
        if (searchTerm.toLowerCase().contains("under")) {
            String[] parts = searchTerm.split("under");
            searchTerm = parts[0].trim(); // Get the term before "under"
            try {
                priceLimit = new BigDecimal(parts[1].trim());
            } catch (NumberFormatException e) {
                // Handle invalid price input
               return productRepository.findByNameContaining(searchTerm);
            }
        }

        // Search by name and category
        List<ProductIndex> byName = productRepository.findByNameContaining(searchTerm);
        List<ProductIndex> byCategory = productRepository.findByCategoryContaining(searchTerm);
        List<ProductIndex> byDesc = productRepository.findByDescriptionContaining(searchTerm);
        byName.addAll(byDesc);

        // Combine search results
        byName.addAll(byCategory);

        // If priceLimit is specified, filter results by MRP (under the price)
        final BigDecimal finalPriceLimit = priceLimit; // Create a final reference for use in lambda
        if (finalPriceLimit != null) {
            byName = byName.stream()
                    .filter(product -> product.getMrp().compareTo(finalPriceLimit) < 0)
                    .collect(Collectors.toList());
        }

        return byName;
    }
    
}
