package com.travel.product.dto.request;

import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class PeriodOptionDTO {

    @NotBlank
    private String optionName;

    @NotBlank
    private String periodString;

    @Future
    private LocalDate startDate;

    @Future
    private LocalDate endDate;

    @Positive
    private Integer maximumQuantity;

    @Positive
    private Integer minimumQuantity;

    @NotBlank
    private String periodOptionStatus;

    public void setDates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        String[] parts = periodString.split(" ");

        startDate = LocalDate.parse(parts[0], formatter);
        endDate = LocalDate.parse(parts[2], formatter);
    }
}
