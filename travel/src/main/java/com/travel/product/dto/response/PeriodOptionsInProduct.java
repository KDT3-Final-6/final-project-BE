package com.travel.product.dto.response;

import com.travel.product.entity.PeriodOption;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PeriodOptionsInProduct {

    private Long periodOptionId;

    private String optionName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String startDetail;

    private String endDetail;

    private String startAirline;

    private String endAirline;

    private Integer period;

    //최대 인원
    private Integer maximumQuantity;

    //최소 인원
    private Integer minimumQuantity;

    //현재 신청한 인원
    private Integer soldQuantity;

    private String periodOptionStatus;

    public PeriodOptionsInProduct(PeriodOption periodOption) {
        this.periodOptionId = periodOption.getPeriodOptionId();
        this.optionName = periodOption.getOptionName();
        this.startDate = periodOption.getStartDate();
        this.endDate = periodOption.getEndDate();
        this.startDetail = periodOption.getStartDetail();
        this.endDetail = periodOption.getEndDetail();
        this.startAirline = periodOption.getStartAirline();
        this.endAirline = periodOption.getEndAirline();
        this.period = periodOption.getPeriod();
        this.maximumQuantity = periodOption.getMaximumQuantity();
        this.minimumQuantity = periodOption.getMinimumQuantity();
        this.soldQuantity = periodOption.getSoldQuantity();
        this.periodOptionStatus = periodOption.getPeriodOptionStatus().getKorean();
    }
}
