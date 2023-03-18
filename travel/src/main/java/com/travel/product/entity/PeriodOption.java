package com.travel.product.entity;

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
@Table(name = "period_option")
public class PeriodOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "period_option_id")
    private Long periodOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "period")
    private Integer period;

    //최대 인원
    @Column(name = "maximum_quantity")
    private Integer maximumQuantity;

    //최소 인원
    @Column(name = "minimum_quantity")
    private Integer minimumQuantity;

    //현재 신청한 인원
    @Column(name = "sold_quantity")
    private Integer soldQuantity;

    public void setProduct(Product product) {
        this.product = product;
    }

    @Builder
    public PeriodOption(Product product, String optionName, LocalDate startDate, LocalDate endDate, Integer maximumQuantity, Integer minimumQuantity) {
        this.product = product;
        this.optionName = optionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = Period.between(startDate, endDate).getDays() + 1;
        this.maximumQuantity = maximumQuantity;
        this.minimumQuantity = minimumQuantity;
        this.soldQuantity = 0;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}