package com.example.ecombackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecombackend.DAO.ProductRepository;
import com.example.ecombackend.model.Product;
import com.example.ecombackend.model.ProductIndex;
import com.example.ecombackend.model.SearchResponse;
import com.example.ecombackend.repository.elastic.ElasticProductRepository;

import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ElasticProductService {

    @Autowired
    private ElasticProductRepository productRepository;

    @Autowired
    private ProductRepository mysqlProductRepository;

    @Transactional
    public void syncProductsToElasticSearch() {
        
        List<Product> products = mysqlProductRepository.findAll();

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
//    public List<ProductIndex> getAllProductsFromElastic() {
//        return (List<ProductIndex>) productRepository.findAll();
//    }
//    public List<ProductIndex> searchAllFields(String searchTerm) {
//        Double priceLimit = null;
//        boolean isUnder = false;
//        boolean isAbove = false;
//        if (searchTerm.toLowerCase().contains("under")) {
//            String[] parts = searchTerm.split("under");
//            searchTerm = parts[0].trim();
//            try {
//                priceLimit = Double.valueOf(parts[1].trim());
//                isUnder = true;
//            } catch (NumberFormatException e) {
//                priceLimit = null;
//            }
//        } else if (searchTerm.toLowerCase().contains("above")) {
//            String[] parts = searchTerm.split("above");
//            searchTerm = parts[0].trim();
//            try {
//                priceLimit = Double.valueOf(parts[1].trim());
//                isAbove = true;
//            } catch (NumberFormatException e) {
//                priceLimit = null;
//            }
//        }
//        searchTerm = searchTerm.trim();
//        List<ProductIndex> byName, byCategory, byDescription;
//        if (searchTerm.contains(" ")) {
//            byName = productRepository.findByNameMatchPhrase(searchTerm);
//            
//            byCategory = productRepository.findByCategoryMatchPhrase(searchTerm);
//            byDescription = productRepository.findByDescriptionMatchPhrase(searchTerm);
//        } else {
//            byName = productRepository.findByNameContaining(searchTerm);
//            byCategory = productRepository.findByCategoryContaining(searchTerm);
//            byDescription = productRepository.findByDescriptionContaining(searchTerm);
//        }
//        byName.addAll(byCategory);
//        byName.addAll(byDescription);
//     
//
//        final BigDecimal finalPriceLimit = Optional.ofNullable(priceLimit)
//                .map(BigDecimal::new)
//                .orElse(BigDecimal.ZERO); // or any default value
//        
//        if (finalPriceLimit != null) {
//            if (isUnder) {
//                byName = byName.stream().distinct()
//                        .filter(product -> product.getMrp().compareTo(finalPriceLimit) < 0)
//                        .collect(Collectors.toList());
//            } else if (isAbove) {
//                              byName = byName.stream()
//                        .filter(product -> product.getMrp().compareTo(finalPriceLimit) > 0)
//                        .collect(Collectors.toList());
//            }
//        }
//   
//
//
//
//        return byName;
//    }
    public List<ProductIndex> getAllProductsFromElastic() {
        return (List<ProductIndex>) productRepository.findAll();
    }
    public SearchResponse searchAllFields2(String searchTerm) {
        Double priceLimit = null;
        boolean isUnder = false;
        boolean isAbove = false;

        // Handle "under" or "above" price conditions
        if (searchTerm.toLowerCase().contains("under")) {
            String[] parts = searchTerm.split("under");
            searchTerm = parts[0].trim();
            try {
                priceLimit = Double.valueOf(parts[1].trim());
                isUnder = true;
            } catch (NumberFormatException e) {
                priceLimit = null;
            }
        } else if (searchTerm.toLowerCase().contains("above")) {
            String[] parts = searchTerm.split("above");
            searchTerm = parts[0].trim();
            try {
                priceLimit = Double.valueOf(parts[1].trim());
                isAbove = true;
            } catch (NumberFormatException e) {
                priceLimit = null;
            }
        }

        searchTerm = searchTerm.trim();

        // Initialize lists for primary search results
        List<ProductIndex> byName;
        List<ProductIndex> byCategory;
        List<ProductIndex> byDescription;

        // Perform searches based on whether the search term contains spaces
        if (searchTerm.contains(" ")) {
            byName = productRepository.findByNameMatchPhrase(searchTerm);
            byCategory = productRepository.findByCategoryMatchPhrase(searchTerm);
            byDescription = productRepository.findByDescriptionMatchPhrase(searchTerm);
        } else {
            byName = productRepository.findByNameContaining(searchTerm);
            byCategory = productRepository.findByCategoryContaining(searchTerm);
            byDescription = productRepository.findByDescriptionContaining(searchTerm);
        }

        // Combine results from all three searches
        List<ProductIndex> combinedResults = new ArrayList<>();
        combinedResults.addAll(byName);
        combinedResults.addAll(byCategory);
        combinedResults.addAll(byDescription);

        // Apply price filter if provided
        BigDecimal finalPriceLimit = Optional.ofNullable(priceLimit)
                .map(BigDecimal::new)
                .orElse(BigDecimal.ZERO); 

        if (finalPriceLimit != null) {
            if (isUnder) {
                combinedResults = combinedResults.stream()
                        .filter(product -> product.getMrp().compareTo(finalPriceLimit) < 0)
                        .distinct()
                        .collect(Collectors.toList());
            } else if (isAbove) {
                combinedResults = combinedResults.stream()
                        .filter(product -> product.getMrp().compareTo(finalPriceLimit) > 0)
                        .distinct()
                        .collect(Collectors.toList());
            }
        }

        // Create a distinct set for final primary results
        Set<ProductIndex> primaryResultsSet = new LinkedHashSet<>(combinedResults);

        List<ProductIndex> similarProducts = new ArrayList<>();
        if (primaryResultsSet.isEmpty()) {
            similarProducts = productRepository.findSimilarProducts(searchTerm);
        } else {
            ProductIndex matchedProduct = primaryResultsSet.iterator().next();
            String matchedCategory = matchedProduct.getCategory();
            String matchedDescription = matchedProduct.getDescription();

           
            List<ProductIndex> categorySimilarProducts = productRepository.findSimilarProducts(matchedCategory);
            List<ProductIndex> descriptionSimilarProducts = productRepository.findSimilarProducts(matchedDescription);

            similarProducts.addAll(categorySimilarProducts);
            similarProducts.addAll(descriptionSimilarProducts);
        }
        final String txt= combinedResults.get(0).getCategory();
        
        similarProducts = similarProducts.stream().distinct().filter(p->p.getCategory().contains(txt)).collect(Collectors.toList());
        
        // Return the combined response
        return new SearchResponse(new ArrayList<>(primaryResultsSet), similarProducts);
    }


    public List<ProductIndex> searchAllFields(String searchTerm) {
        Double priceLimit = null;
        boolean isUnder = false;
        boolean isAbove = false;

        // Handle "under" or "above" price conditions
        if (searchTerm.toLowerCase().contains("under")) {
            String[] parts = searchTerm.split("under");
            searchTerm = parts[0].trim();
            try {
                priceLimit = Double.valueOf(parts[1].trim());
                isUnder = true;
            } catch (NumberFormatException e) {
                priceLimit = null;
            }
        } else if (searchTerm.toLowerCase().contains("above")) {
            String[] parts = searchTerm.split("above");
            searchTerm = parts[0].trim();
            try {
                priceLimit = Double.valueOf(parts[1].trim());
                isAbove = true;
            } catch (NumberFormatException e) {
                priceLimit = null;
            }
        }

        searchTerm = searchTerm.trim();
        List<ProductIndex> primarySearchResults;

        // Primary search (based on the search term)
        if (searchTerm.contains(" ")) {
            primarySearchResults = productRepository.findByNameMatchPhrase(searchTerm);
        } else {
            primarySearchResults = productRepository.findByNameContaining(searchTerm);
        }

        // Debugging output for primary search results
        System.out.println("Primary search results: " + primarySearchResults);

        // Apply price filter
        if (priceLimit != null) {
            BigDecimal finalPriceLimit = BigDecimal.valueOf(priceLimit);
            if (isUnder) {
                primarySearchResults = primarySearchResults.stream()
                        .filter(product -> product.getMrp().compareTo(finalPriceLimit) < 0)
                        .collect(Collectors.toList());
            } else if (isAbove) {
                primarySearchResults = primarySearchResults.stream()
                        .filter(product -> product.getMrp().compareTo(finalPriceLimit) > 0)
                        .collect(Collectors.toList());
            }
        }

        // Debugging output for filtered primary search results
        System.out.println("Filtered primary search results: " + primarySearchResults);

        // Create a distinct set to hold results and ensure no duplicates
        Set<ProductIndex> finalResults = new LinkedHashSet<>(primarySearchResults);

        // If primary results are empty, search for similar products based on search term
        if (primarySearchResults.isEmpty()) {
            List<ProductIndex> similarProducts = productRepository.findSimilarProducts(searchTerm);
            finalResults.addAll(similarProducts);
        } else {
            // If primary results are found, fetch similar products based on their category or description
            ProductIndex matchedProduct = primarySearchResults.get(0); // Taking the first match
            String matchedCategory = matchedProduct.getCategory();
            String matchedDescription = matchedProduct.getDescription();

            // Fetch similar products
            List<ProductIndex> categorySimilarProducts = productRepository.findSimilarProducts(matchedCategory);
            List<ProductIndex> descriptionSimilarProducts = productRepository.findSimilarProducts(matchedDescription);

            // Add similar products to final results
            finalResults.addAll(categorySimilarProducts);
            finalResults.addAll(descriptionSimilarProducts);
        }

        // Ensure distinct results and return, maintaining the order
        List<ProductIndex> resultList = new ArrayList<>(finalResults);
        System.out.println("Final results: " + resultList);
        return resultList;
    }


}

    

