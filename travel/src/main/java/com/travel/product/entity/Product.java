package com.travel.product.entity;

import com.travel.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

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

    //최대 인원
    @Column(name = "maximum_quantity")
    private Integer productMaximumQuantity;

    //최소 인원
    @Column(name = "minimum_quantity")
    private Integer productMinimumQuantity;

    //현재 신청한 인원
    @Column(name = "sold_quantity")
    private Integer productSoldQuantity;

    @Column(name = "product_detail")
    private String productDetail;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "period")
    private Integer period;

    @Builder
    public Product(String productName, String productThumbnail, Integer productPrice, Category productCategory, Status productStatus, Integer productMaximumQuantity, Integer productMinimumQuantity, Integer productSoldQuantity, String productDetail, LocalDate startDate, LocalDate endDate) {
        this.productName = productName;
        this.productThumbnail = productThumbnail;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productStatus = productStatus;
        this.productMaximumQuantity = productMaximumQuantity;
        this.productMinimumQuantity = productMinimumQuantity;
        this.productSoldQuantity = productSoldQuantity;
        this.productDetail = productDetail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = Period.between(startDate, endDate).getDays() + 1;

    }

    /*
    @ElementCollection
    @CollectionTable(name = "image")
    private List<String> images = new ArrayList<>();
    */
}
