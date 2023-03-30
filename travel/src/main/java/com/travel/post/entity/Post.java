package com.travel.post.entity;

import com.travel.global.entity.BaseEntityWithMemberAndDates;
import com.travel.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "post")
@DiscriminatorColumn(name = "post_type")
public class Post extends BaseEntityWithMemberAndDates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "post_content")
    private String postContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @Column(name = "is_Canceled")
    private boolean isCanceled = false;

    @Builder
    public Post(String postTitle, String postContent, Member member) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.member = member;
    }
}
