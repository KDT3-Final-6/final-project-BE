package com.travel.member.dto.requestDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class PasswordCheckDTO {
    private String memberPassword;

    public String getMemberPassword() {
        return memberPassword;
    }
}
