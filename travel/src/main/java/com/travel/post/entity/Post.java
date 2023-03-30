package com.travel.post.entity;

import com.travel.global.entity.BaseEntityWithMemberAndDates;
import com.travel.member.entity.Member;
import com.travel.product.entity.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String title;

    @Column(name = "post_content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
