package com.example.ecombackend.repository.elastic;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.ecombackend.model.ProductIndex;

public interface ElasticProductRepository extends ElasticsearchRepository<ProductIndex, String> {

	    List<ProductIndex> findByNameContaining(String name);
	    List<ProductIndex> findByDescriptionContaining(String description);
	    List<ProductIndex> findByCategoryContaining(String category);
	    List<ProductIndex> findByMrpBetween(BigDecimal minMrp, BigDecimal maxMrp);
	    @Query("{\"match_phrase\": {\"name\": \"?0\"}}")
	    List<ProductIndex> findByNameMatchPhrase(String name);
	    @Query("{\"match_phrase\": {\"category\": \"?0\"}}")
	    List<ProductIndex> findByCategoryMatchPhrase(String category);
	    @Query("{\"match_phrase\": {\"description\": \"?0\"}}")
	    List<ProductIndex> findByDescriptionMatchPhrase(String description);
	    @Query("{\"bool\": {\"should\": ["
	            + "{\"match\": {\"category\": \"?1\"}},"
	            + "{\"match\": {\"description\": {\"query\": \"?2\", \"fuzziness\": \"AUTO\"}}}]}}")
	    List<ProductIndex> findSimilarProductsByCategoryAndDescription(String category, String description);
	    @Query("{\"bool\": {\"should\": ["
	            + "{\"match\": {\"category\": \"?0\"}},"
	            + "{\"match\": {\"description\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}]}}")
	    List<ProductIndex> findSimilarProducts(String searchTerm);

   
}
