package com.travel.post.repository;

import com.travel.member.entity.Member;
import com.travel.post.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByMemberAndPostId(Member member, Long postId);
    @Query("SELECT COUNT(p) FROM Post p WHERE type(p) IN QnAPost and p.member.memberId = :memberId")
    Long countQnaPostsByMemberId(@Param("memberId") Long memberId);
    @Query("SELECT COUNT(r) FROM Post r WHERE type(r) IN ReviewPost and r.member.memberId = :memberId")
    Long countReviewPostsByMemberId(@Param("memberId") Long memberId);


}
