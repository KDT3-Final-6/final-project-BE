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

    /*
    @ElementCollection
    @CollectionTable(name = "image")
    private List<String> images = new ArrayList<>();
    */

    @Column(name = "product_price")
    private Integer productPrice;

    @Column(name = "product_status")
    @Enumerated(EnumType.STRING)
    private Status productStatus;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<PeriodOption> periodOptions = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductCategory> productCategories = new ArrayList<>();

    //제목
    @Column(name = "product_content")
    private String productContent;

    //상세
    @Column(name = "content_detail")
    private String contentDetail;

    public void addPeriodOption(PeriodOption periodOption) {
        periodOption.setProduct(this);
        periodOptions.add(periodOption);
    }

    @Builder
    public Product(String productName, String productThumbnail, Integer productPrice, Status productStatus, String productContent, String contentDetail) {
        this.productName = productName;
        this.productThumbnail = productThumbnail;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productContent = productContent;
        this.contentDetail = contentDetail;
    }
}
