package com.travel.post.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class QnAAdminResponseDTO extends QnAResponseDTO {

    private String memberName;
}
