package com.travel.post.entity;

import lombok.*;

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

    @Setter
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
