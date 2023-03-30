package com.travel.post.entity;


import com.travel.product.entity.PurchasedProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "qna_product_post")
@DiscriminatorValue("QP")
public class QnAProductPost extends QnAPost {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchased_product_id")
    private PurchasedProduct purchasedProduct;

    public QnAProductPost(QnAPost qnAPost, PurchasedProduct purchasedProduct) {
        super(qnAPost.getPostTitle(), qnAPost.getPostContent(), qnAPost.getMember(), qnAPost.getInquiryType());
        this.purchasedProduct = purchasedProduct;
    }
}
