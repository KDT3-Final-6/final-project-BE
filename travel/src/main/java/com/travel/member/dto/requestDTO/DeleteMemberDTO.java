package com.travel.member.dto.requestDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeleteMemberDTO {
    private String memberPassword;
    private String accessToken;
}