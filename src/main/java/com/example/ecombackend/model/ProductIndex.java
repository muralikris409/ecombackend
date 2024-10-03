package com.example.ecombackend.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products") // This specifies the index name in ElasticSearch
public class ProductIndex {

    @Id // This marks the id field as the document ID in ElasticSearch
    private Long id;

    @Field(type = FieldType.Text) // Use Text type for full-text search capabilities
    private String name;

    @Field(type = FieldType.Text) // Use Text type for description
    private String description;

    @Field(type = FieldType.Double) // Use Double type for decimal values
    private BigDecimal mrp;

    @Field(type = FieldType.Double) // Use Double type for discount
    private BigDecimal discount;

    @Field(type = FieldType.Integer) // Use Integer type for stock
    private Integer stock;

    // You can include the category if needed, but you might want to store it differently
    @Field(type = FieldType.Text) // Assuming you want to index category as well
    private String category; // Change this if you have a separate category model

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMrp() {
        return mrp;
    }

    public void setMrp(BigDecimal mrp) {
        this.mrp = mrp;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
