package com.travel.product.dto.request;

import com.travel.product.entity.PeriodOption;
import com.travel.product.entity.Status;
import lombok.Getter;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class PeriodPostRequestDTO {
    private Long productId;

    @Valid
    private List<PeriodOptionDTO> options = new ArrayList<>();

    public List<PeriodOption> toEntities() {

//        List<PeriodOption> periodOptionList = new ArrayList<>();
//        for(PeriodOptionPostRequestDTO periodOption : periodOptions){
//            periodOption.setDates();
//            //request로 받은걸 파싱해서 Date에 다시 넣는용도
//            periodOptionList.add(
//                    PeriodOption.builder()
//                            .startDate(periodOption.getStartDate())
//                            .endDate(periodOption.getEndDate())
//                            .maximumQuantity(periodOption.getMaximumQuantity())
//                            .minimumQuantity(periodOption.getMinimumQuantity())
//                            .build()
//            );
//        }

        List<PeriodOption> periodOptionList = options.stream()
                .peek(PeriodOptionDTO::setDates)
                .map(periodOption -> PeriodOption.builder()
                        .optionName(periodOption.getOptionName())
                        .startDate(periodOption.getStartDate())
                        .endDate(periodOption.getEndDate())
                        .startDetail(periodOption.getStartDetail())
                        .endDetail(periodOption.getEndDetail())
                        .startAirline(periodOption.getStartAirline())
                        .endAirline(periodOption.getEndAirline())
                        .maximumQuantity(periodOption.getMaximumQuantity())
                        .minimumQuantity(periodOption.getMinimumQuantity())
                        .periodOptionStatus(setEnumProductStatus(periodOption.getPeriodOptionStatus()))
                        .build())
                .collect(toList());

        return periodOptionList;
    }

    public Status setEnumProductStatus(String productStatus) {
        if (productStatus.equals(Status.FORSALE.getKorean())) {
            return Status.FORSALE;
        } else if (productStatus.equals(Status.SOLDOUT.getKorean())) {
            return Status.SOLDOUT;
        }
        return Status.HIDDEN;
    }
}
