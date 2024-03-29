package com.travel.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "start_detail")
    private String startDetail;

    @Column(name = "end_detail")
    private String endDetail;

    @Column(name = "start_airline")
    private String startAirline;

    @Column(name = "end_airline")
    private String endAirline;

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

    @Setter
    @Column(name = "period_option_status")
    @Enumerated(EnumType.STRING)
    private Status periodOptionStatus;

    public void setProduct(Product product) {
        this.product = product;
    }

    @Builder
    public PeriodOption(Product product, String optionName, LocalDate startDate, LocalDate endDate, Integer maximumQuantity, Integer minimumQuantity, Status periodOptionStatus
            , String startDetail, String endDetail, String startAirline, String endAirline) {
        this.product = product;
        this.optionName = optionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = Period.between(startDate, endDate).getDays() + 1;
        this.maximumQuantity = maximumQuantity;
        this.minimumQuantity = minimumQuantity;
        this.periodOptionStatus = periodOptionStatus;
        this.soldQuantity = 0;
        this.startDetail = startDetail;
        this.endDetail = endDetail;
        this.startAirline = startAirline;
        this.endAirline = endAirline;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}