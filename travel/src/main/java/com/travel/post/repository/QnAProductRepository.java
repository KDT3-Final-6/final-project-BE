package com.travel.post.repository;

import com.travel.post.entity.QnAProductPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnAProductRepository extends JpaRepository<QnAProductPost, Long> {
}
