package com.travel.product.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "purchased_product")
@Entity
public class PurchasedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchased_product_id")
    private Long purchasedProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "purchased_product_name")
    private String purchasedProductName;

    @Column(name = "purchased_product_thumbnail")
    private String purchasedProductThumbnail;

    @Column(name = "purchased_product_price")
    private Integer purchasedProductPrice;

    @Column(name = "purchased_product_quantity")
    private Integer productProductQuantity;

    public PurchasedProduct(Product product) {
        this.purchasedProductName = product.getProductName();
        this.purchasedProductThumbnail = product.getProductThumbnail();
        this.purchasedProductPrice = product.getProductPrice();
        this.productProductQuantity = 3;
    }
}
