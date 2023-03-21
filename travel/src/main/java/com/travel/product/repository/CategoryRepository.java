package com.travel.product.repository;

import com.travel.product.entity.Category;
import com.travel.product.entity.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryEnum(CategoryEnum categoryEnum);
}
