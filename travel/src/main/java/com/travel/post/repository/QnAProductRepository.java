package com.travel.post.repository;

import com.travel.post.entity.QnAPost;
import com.travel.post.entity.QnAProductPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnAProductRepository extends JpaRepository<QnAProductPost, Long> {

    List<QnAPost> findByPurchasedProductPurchasedProductNameContaining(String keyword);
}
