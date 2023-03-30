package com.travel.product.repository;

import com.travel.product.entity.Category;
import com.travel.product.entity.Product;
import com.travel.product.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findAllByCategory(Category category);
    void deleteAllByProduct(Product product);
}
