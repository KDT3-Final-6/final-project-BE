package com.travel.post.repository;

import com.travel.post.entity.QnAPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnARepository extends JpaRepository<QnAPost, Long> {
}
