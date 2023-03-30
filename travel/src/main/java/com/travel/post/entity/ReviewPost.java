package com.travel.post.entity;

import com.travel.product.entity.PurchasedProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "review_scope")
    @Enumerated(EnumType.STRING)
    private Scope scope;
}
