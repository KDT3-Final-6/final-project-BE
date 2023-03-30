package com.travel.post.repository;

import com.travel.member.entity.Member;
import com.travel.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByMemberAndPostId(Member member, Long postId);
}
