package com.travel.curation.repository;

import com.travel.product.entity.Product;
import com.travel.survey.entity.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CurationRepositoryCustom {

    Page<Product> findAllWithThreeCategories(Pageable pageable, String season, String district, String theme);
    Page<Product> findAllWithTarget(Pageable pageable, Survey survey, String target);
    Page<Product> findAllWithSeason(Pageable pageable, Survey survey);
    Page<Product> findAllWithGroupAndThemes(Pageable pageable, String group, List<String> conceptList);
}
