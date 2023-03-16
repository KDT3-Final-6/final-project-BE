package com.travel.product.entity;

import com.travel.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_thumbnail")
    private String productThumbnail;

    @Column(name = "product_price")
    private Integer productPrice;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private Category productCategory;

    @Column(name = "product_status")
    @Enumerated(EnumType.STRING)
    private Status productStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    private List<PeriodOption> periodOptions = new ArrayList<>();

    @Column(name = "product_detail")
    private String productDetail;

    @Builder
    public Product(String productName, String productThumbnail, Integer productPrice, Category productCategory, String productDetail) {
        this.productName = productName;
        this.productThumbnail = productThumbnail;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productDetail = productDetail;
    }
    /*
    @ElementCollection
    @CollectionTable(name = "image")
    private List<String> images = new ArrayList<>();
    */
}
