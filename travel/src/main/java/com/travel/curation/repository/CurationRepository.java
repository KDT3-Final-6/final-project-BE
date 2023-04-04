package com.travel.curation.repository;

import com.travel.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurationRepository extends JpaRepository<Product, Long>, CurationRepositoryCustom{
}
