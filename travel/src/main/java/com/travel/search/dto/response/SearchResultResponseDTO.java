package com.travel.search.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResultResponseDTO {

    private Long productId;

    private String productName;

    private String productThumbnail;

    private Integer productPrice;

    private Boolean isWished;

}
