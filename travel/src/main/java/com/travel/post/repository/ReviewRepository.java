package com.travel.post.repository;

import com.travel.member.entity.Member;
import com.travel.post.entity.ReviewPost;
import com.travel.product.entity.Product;
import com.travel.product.entity.PurchasedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewPost, Long> {

    boolean existsByPurchasedProduct(PurchasedProduct purchasedProduct);

    Optional<ReviewPost> findByPostIdAndMember(Long postId, Member member);

    List<ReviewPost> findByMember(Member member);

    List<ReviewPost> findByPurchasedProductProduct(Product product);
}
