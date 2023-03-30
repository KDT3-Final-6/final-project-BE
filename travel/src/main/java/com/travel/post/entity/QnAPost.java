package com.travel.post.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
