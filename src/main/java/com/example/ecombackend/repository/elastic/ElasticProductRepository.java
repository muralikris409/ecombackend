package com.example.ecombackend.repository.elastic;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.ecombackend.model.Product;
import com.example.ecombackend.model.ProductIndex;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.ecombackend.model.ProductIndex;

public interface ElasticProductRepository extends ElasticsearchRepository<ProductIndex, String> {
   

    List<ProductIndex> findByNameContaining(String name);
    List<ProductIndex> findByDescriptionContaining(String description);
    List<ProductIndex> findByCategoryContaining(String category);
    List<ProductIndex> findByMrpBetween(BigDecimal minMrp, BigDecimal maxMrp);
}

