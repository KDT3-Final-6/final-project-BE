package com.travel.post.entity;

import com.travel.member.entity.Member;
import com.travel.post.dto.response.ReviewListDTO;
import com.travel.post.dto.response.ReviewInMemberDTO;
import com.travel.post.dto.response.ReviewInProductDTO;
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

    public ReviewListDTO toReviewListDTO() {
        return ReviewListDTO.builder()
                .postId(this.getPostId())
                .productId(this.purchasedProduct.getProduct().getProductId())
                .purchasedProductName(this.purchasedProduct.getPurchasedProductName())
                .purchasedProductThumbnail(this.purchasedProduct.getPurchasedProductThumbnail())
                .memberNickname(this.getMember().getMemberNickname())
                .postContent(this.getPostContent())
                .scope(this.scope)
                .modifiedDate(this.getModifiedDate().toLocalDate())
                .build();
    }

    public ReviewInMemberDTO toReviewListMemberDTO() {
        return ReviewInMemberDTO.builder()
                .postId(this.getPostId())
                .productId(this.purchasedProduct.getProduct().getProductId())
                .purchasedProductName(this.purchasedProduct.getPurchasedProductName())
                .purchasedProductThumbnail(this.purchasedProduct.getPurchasedProductThumbnail())
                .postContent(this.getPostContent())
                .scope(this.scope)
                .modifiedDate(this.getModifiedDate().toLocalDate())
                .build();
    }

    public ReviewInProductDTO toReviewListProductDTO() {
        return ReviewInProductDTO.builder()
                .memberNickname(this.getMember().getMemberNickname())
                .postContent(this.getPostContent())
                .scope(this.scope)
                .build();
    }
}
