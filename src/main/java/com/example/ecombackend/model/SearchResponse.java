package com.example.ecombackend.model;
import java.util.*;
public class SearchResponse {
    private List<ProductIndex> primary;
    private List<ProductIndex> similar;

    // Constructor
    public SearchResponse(List<ProductIndex> primary, List<ProductIndex> similar) {
        this.primary = primary;
        this.similar = similar;
    }

    // Getters and Setters
    public List<ProductIndex> getPrimary() {
        return primary;
    }

    public void setPrimary(List<ProductIndex> primary) {
        this.primary = primary;
    }

    public List<ProductIndex> getSimilar() {
        return similar;
    }

    public void setSimilar(List<ProductIndex> similar) {
        this.similar = similar;
    }
}
