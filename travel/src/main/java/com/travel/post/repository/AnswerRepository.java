package com.travel.post.repository;

import com.travel.post.entity.AnswerPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<AnswerPost, Long> {
}
