package com.travel.wishlist.entity;

import com.travel.member.entity.Member;
import com.travel.product.entity.Product;
import com.travel.wishlist.dto.response.WishlistResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "wishlist")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long wishlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public Wishlist(Member member, Product product) {
        this.member = member;
        this.product = product;
    }

    public WishlistResponseDTO toResponseDTO() {
        return WishlistResponseDTO.builder()
                .wishilistId(this.wishlistId)
                .productId(this.product.getProductId())
                .productName(this.product.getProductName())
                .productThumbnail(this.product.getProductThumbnail())
                .productPrice(this.product.getProductPrice())
                .build();
    }
}