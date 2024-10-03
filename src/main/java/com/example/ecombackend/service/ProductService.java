package com.example.ecombackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.ecombackend.DAO.ProductRepository;
import com.example.ecombackend.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
//    @Autowired
//    private SolrProductRepository solrProductRepository;
//
//    public List<Product> getSolrAllProductsSorted() {
//        // Example of sorting by 'name' in ascending order
//        Iterable<Product> iterable = solrProductRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
//        return StreamSupport
//                .stream(iterable.spliterator(), false)
//                .collect(Collectors.toList());
//    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
  

  
}

