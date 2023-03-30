package com.travel.post.entity;

import com.travel.member.entity.Member;
import com.travel.post.dto.response.QnAAdminResponseDTO;
import com.travel.post.dto.response.QnAResponseDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "qna_post")
@DiscriminatorValue("Q")
public class QnAPost extends Post {

    @Column(name = "qna_inquiry_type")
    @Enumerated(EnumType.STRING)
    private InquiryType inquiryType;

    @Setter
    @Column(name = "qna_status")
    @Enumerated(EnumType.STRING)
    private QnAStatus qnAStatus = QnAStatus.WAITING_FOR_ANSWER;

    public QnAPost(String title, String content, Member member, InquiryType inquiryType) {
        super(title, content, member);
        this.inquiryType = inquiryType;
    }

    public QnAResponseDTO toQnAResponseDTO() {
        return QnAResponseDTO.builder()
                .postId(this.getPostId())
                .postTitle(this.getPostTitle())
                .postContent(this.getPostContent())
                .inquiryType(this.getInquiryType().getKorean())
                .qnAStatus(this.getQnAStatus().getKorean())
                .createdDate(this.getCreatedDate())
                .build();
    }

    public QnAAdminResponseDTO toQnAAdminResponseDTO() {
        return QnAAdminResponseDTO.builder()
                .postId(this.getPostId())
                .postTitle(this.getPostTitle())
                .postContent(this.getPostContent())
                .inquiryType(this.getInquiryType().getKorean())
                .qnAStatus(this.getQnAStatus().getKorean())
                .createdDate(this.getCreatedDate())
                .memberName(this.getMember().getMemberName())
                .build();
    }
}
