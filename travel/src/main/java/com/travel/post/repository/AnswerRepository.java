package com.travel.post.repository;

import com.travel.post.entity.AnswerPost;
import com.travel.post.entity.QnAPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<AnswerPost, Long> {

    Optional<AnswerPost> findByQnAPost(QnAPost qnAPost);
}
