package com.travel.post.repository;

import com.travel.member.entity.Member;
import com.travel.post.entity.QnAPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnARepository extends JpaRepository<QnAPost, Long> {

    List<QnAPost> findByMember(Member member);
}
