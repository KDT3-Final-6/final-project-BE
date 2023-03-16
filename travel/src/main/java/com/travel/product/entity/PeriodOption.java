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

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "period")
    private Integer period;

    //최대 인원
    @Column(name = "period_option_maximum_quantity")
    private Integer periodOptionMaximumQuantity;

    //최소 인원
    @Column(name = "period_option_minimum_quantity")
    private Integer periodOptionMinimumQuantity;

    //현재 신청한 인원
    @Column(name = "period_option_sold_quantity")
    private Integer periodOptionSoldQuantity;

    @Builder
    public PeriodOption(Product product, LocalDate startDate, LocalDate endDate, Integer periodOptionMaximumQuantity, Integer periodOptionMinimumQuantity, Integer periodOptionSoldQuantity) {
        this.product = product;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = Period.between(startDate, endDate).getDays() + 1;
        this.periodOptionMaximumQuantity = periodOptionMaximumQuantity;
        this.periodOptionMinimumQuantity = periodOptionMinimumQuantity;
        this.periodOptionSoldQuantity = periodOptionSoldQuantity;
    }
}