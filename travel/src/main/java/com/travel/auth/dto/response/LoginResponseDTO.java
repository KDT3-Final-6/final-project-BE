package com.travel.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private final String grantType;
    private final String accessToken;
    private final String refreshToken;
    private final Long refreshTokenExpirationTime;

    public LoginResponseDTO(MemberResponseDto.TokenInfo tokenInfo) {
        this.grantType = tokenInfo.getGrantType();
        this.accessToken = tokenInfo.getAccessToken();
        this.refreshToken = tokenInfo.getRefreshToken();
        this.refreshTokenExpirationTime = tokenInfo.getRefreshTokenExpirationTime();
    }

}