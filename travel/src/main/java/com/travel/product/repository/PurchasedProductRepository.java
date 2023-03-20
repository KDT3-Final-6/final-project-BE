package com.travel.product.repository;

import com.travel.order.entity.Order;
import com.travel.product.entity.PurchasedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, Long> {

    List<PurchasedProduct> findByOrder(Order order);
}
