package com.app.repository;

import com.app.model.PatronElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PatronElasticRepository extends ElasticsearchRepository<PatronElastic, Integer> {
}
