package com.travel.curation.repository;

import com.travel.product.entity.Product;
import com.travel.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CurationRepositoryCustom {

    Page<Product> findAllWithTarget(Pageable pageable, Survey survey, String target);
    Page<Product> findAllWithSeason(Pageable pageable, Survey survey);
}
