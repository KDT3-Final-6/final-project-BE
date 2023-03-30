package com.travel.post.entity;

import com.travel.product.entity.PurchasedProduct;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "answer_post")
public class AnswerPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "answer_content")
    private String answerContent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private QnAPost qnAPost;

    @Builder
    public AnswerPost(String answerContent, QnAPost qnAPost) {
        this.answerContent = answerContent;
        this.qnAPost = qnAPost;
    }
}
