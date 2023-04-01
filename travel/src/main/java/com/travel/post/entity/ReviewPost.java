package com.travel.post.entity;

import com.travel.member.entity.Member;
import com.travel.post.dto.response.ReviewResponseDTO;
import com.travel.product.entity.PurchasedProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review_post")
@DiscriminatorValue("R")
public class ReviewPost extends Post {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchased_product_id")
    private PurchasedProduct purchasedProduct;

    @Setter
    @Column(name = "review_scope")
    private Integer scope;

    public ReviewPost(String postTitle, String postContent, Member member, PurchasedProduct purchasedProduct, Integer scope) {
        super(postTitle, postContent, member);
        this.purchasedProduct = purchasedProduct;
        this.scope = scope;
    }

    public ReviewResponseDTO toReviewResponseDTO() {
        return ReviewResponseDTO.builder()
                .postId(this.getPostId())
                .productId(this.purchasedProduct.getProduct().getProductId())
                .purchasedProductName(this.purchasedProduct.getPurchasedProductName())
                .postContent(this.getPostContent())
                .scope(this.scope)
                .modifiedDate(this.getModifiedDate().toLocalDate())
                .build();
    }
}
