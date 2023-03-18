package com.travel.product.repository;

import com.travel.product.entity.PurchasedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, Long> {
}
