package com.travel.post.repository;

import com.travel.post.entity.ReviewPost;
import com.travel.product.entity.PurchasedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewPost, Long> {

    boolean existsByPurchasedProduct(PurchasedProduct purchasedProduct);
}
