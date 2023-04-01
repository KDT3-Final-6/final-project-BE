package com.travel.post.repository.qnapost;

import com.travel.member.entity.Member;
import com.travel.post.entity.InquiryType;
import com.travel.post.entity.QnAPost;
import com.travel.post.entity.QnAStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnARepository extends JpaRepository<QnAPost, Long>, QnACustomRepository {

    List<QnAPost> findByMember(Member member);

    List<QnAPost> findByQnAStatus(QnAStatus qnAStatus);

    List<QnAPost> findByInquiryType(InquiryType inquiryType);
}
